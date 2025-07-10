package com.example.stock.screens.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice as AndroidBluetoothDevice
import android.bluetooth.BluetoothManager as AndroidBluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import android.app.Application
import android.bluetooth.BluetoothSocket
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stock.StockApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID
import androidx.core.content.edit
import kotlinx.coroutines.Job

enum class BluetoothState {
    DISABLED,    // 비활성화
    READY,       // 준비 완료
    SEARCHING,   // 검색 중
    CONNECTING, // 연결 시도 중 상태 추가
    CONNECTED,  // 연결 완료 상태 추가
    ERROR        // 오류 발생
}

data class BluetoothDevice(
    val name: String?,
    val address: String,
    val isConnecting: Boolean = false // 개별 장치의 연결 시도 여부
)


class BluetoothManager(private val context: Context) {

    private val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as AndroidBluetoothManager
        manager.adapter
    }

    private val _bluetoothState = MutableStateFlow(BluetoothState.DISABLED)
    val bluetoothState: StateFlow<BluetoothState> = _bluetoothState.asStateFlow()

    private val _discoveredDevices = MutableStateFlow<List<BluetoothDevice>>(emptyList())
    val discoveredDevices: StateFlow<List<BluetoothDevice>> = _discoveredDevices.asStateFlow()

    private var bluetoothSocket: BluetoothSocket? = null
    private var inputStream: InputStream? = null

    private val _scannedDataFlow = MutableSharedFlow<String>(replay = 0)
    val scannedDataFlow: SharedFlow<String> = _scannedDataFlow.asSharedFlow()

    private var isReceiverRegistered = false

    private val deviceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                // 새 장치를 찾았을 때
                AndroidBluetoothDevice.ACTION_FOUND -> {
                    val device: AndroidBluetoothDevice? = intent.getParcelableExtra(AndroidBluetoothDevice.EXTRA_DEVICE)
                    device?.let { addDiscoveredDevice(it) }
                }
                // 장치 검색이 끝났을 때
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> {
                    if (_bluetoothState.value == BluetoothState.SEARCHING) {
                        _bluetoothState.value = BluetoothState.READY
                    }
                }
                BluetoothAdapter.ACTION_STATE_CHANGED -> {
                    updateBluetoothState()
                }
            }
        }
    }

    init {
        registerReceiver()
        updateBluetoothState()
    }

    fun saveLastConnectedDevice(context: Context, deviceAddress: String) {
        val prefs = context.getSharedPreferences("BluetoothPrefs", Context.MODE_PRIVATE)
        prefs.edit { putString("last_connected_device", deviceAddress) }
    }

    fun getLastConnectedDevice(context: Context): String? {
        val prefs = context.getSharedPreferences("BluetoothPrefs", Context.MODE_PRIVATE)
        return prefs.getString("last_connected_device", null)
    }

    fun updateBluetoothState() {
        bluetoothAdapter?.let { adapter ->
            _bluetoothState.value = if (adapter.isEnabled) BluetoothState.READY else BluetoothState.DISABLED
        } ?: run {
            _bluetoothState.value = BluetoothState.ERROR
        }
    }

    fun tryAutoConnect(context: Context, bluetoothManager: BluetoothManager) {
        val lastDeviceAddress = getLastConnectedDevice(context)
        if (!lastDeviceAddress.isNullOrEmpty()) {
            bluetoothManager.connectToDevice(lastDeviceAddress)
        }
    }

    @SuppressLint("MissingPermission")
    fun startDiscovery() {
        if (bluetoothAdapter?.isEnabled == true) {
            _bluetoothState.value = BluetoothState.SEARCHING
            _discoveredDevices.value = emptyList() // 이전 목록 초기화
            bluetoothAdapter?.startDiscovery()
        }
    }

    @SuppressLint("MissingPermission")
    fun stopDiscovery() {
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter?.cancelDiscovery()
        }
        if (_bluetoothState.value == BluetoothState.SEARCHING) {
            _bluetoothState.value = BluetoothState.READY
        }
    }

    @SuppressLint("MissingPermission")
    fun connectToDevice(deviceAddress: String) {
        // 이미 연결 중이거나 연결된 상태면 중복 실행 방지
        if (_bluetoothState.value == BluetoothState.CONNECTING || _bluetoothState.value == BluetoothState.CONNECTED) {
            return
        }

        // UI 업데이트: 연결 시도 중 상태로 변경
        _bluetoothState.value = BluetoothState.CONNECTING
        _discoveredDevices.update { list ->
            list.map { if (it.address == deviceAddress) it.copy(isConnecting = true) else it }
        }

        // 연결 작업은 블로킹(Blocking)될 수 있으므로 IO 스레드에서 실행
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 검색 중지 (연결 안정성을 위해)
                stopDiscovery()

                val device: AndroidBluetoothDevice? = bluetoothAdapter?.getRemoteDevice(deviceAddress)
                bluetoothSocket = device?.createRfcommSocketToServiceRecord(SPP_UUID)

                bluetoothSocket?.connect() // 연결 시도

                inputStream = bluetoothSocket?.inputStream
                startListeningForData() // 데이터 리스닝 시작

                saveLastConnectedDevice(context, deviceAddress)
                // 연결 성공
                _bluetoothState.value = BluetoothState.CONNECTED

            } catch (e: IOException) {
                // 연결 실패
                e.printStackTrace()
                disconnect() // 소켓 정리
                _bluetoothState.value = BluetoothState.ERROR // 또는 READY

            } finally {
                // UI 업데이트: isConnecting 플래그 초기화
                _discoveredDevices.update { list ->
                    list.map { it.copy(isConnecting = false) }
                }
            }
        }
    }

    private var dataListeningJob: Job? = null

    private fun keepAlphanumericOnly(input: String): String {
        // 영숫자, 공백, 하이픈, 점만 허용
        return input.replace("[^A-Za-z0-9 .-]".toRegex(), "")
    }

    private fun startListeningForData() {
        if (inputStream == null) return

        val buffer = ByteArrayOutputStream()
        val delimiter: Byte = 10

        dataListeningJob?.cancel()

        dataListeningJob = CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                try {
                    val byte = inputStream!!.read()
                    if (byte == -1) break

                    if (byte.toByte() == delimiter) {
                        val receivedString = buffer.toString("UTF-8").trim()
                        val cleanedData = keepAlphanumericOnly(receivedString)
                        if (receivedString.isNotEmpty()) {
                            // 정제된 데이터가 비어있지 않은 경우만 전송
                            if (cleanedData.isNotEmpty()) {
                                _scannedDataFlow.emit(cleanedData)
                            }
                        }
                        buffer.reset()
                    } else {
                        buffer.write(byte)
                    }

                } catch (e: IOException) {
                    break
                }
            }
        }
    }

    fun disconnect() {
        try {
            dataListeningJob?.cancel()
            inputStream?.close()
            bluetoothSocket?.close()
            inputStream = null
            bluetoothSocket = null
            dataListeningJob = null
            _bluetoothState.value = BluetoothState.READY
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }



    fun cleanup() {
        try {
            if (isReceiverRegistered) {
                context.unregisterReceiver(deviceReceiver)
                isReceiverRegistered = false
            }
        } catch (e: IllegalArgumentException) {
        }
    }

    private fun registerReceiver() {
        if (!isReceiverRegistered) {
            val filter = IntentFilter().apply {
                addAction(AndroidBluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
            }
            context.registerReceiver(deviceReceiver, filter)
            isReceiverRegistered = true
        }
    }

    @SuppressLint("MissingPermission")
    private fun addDiscoveredDevice(device: AndroidBluetoothDevice) {
        if (device.name == null) return

        val newDevice = BluetoothDevice(
            name = device.name,
            address = device.address
        )

        if (_discoveredDevices.value.none { it.address == newDevice.address }) {
            _discoveredDevices.value = _discoveredDevices.value + newDevice
        }
    }
}

class BluetoothViewModel(internal val bluetoothManager: BluetoothManager) : ViewModel() {

    val bluetoothState: StateFlow<BluetoothState> = bluetoothManager.bluetoothState
    val discoveredDevices: StateFlow<List<BluetoothDevice>> = bluetoothManager.discoveredDevices

    val scannedDataFlow: SharedFlow<String> = bluetoothManager.scannedDataFlow

    fun startDiscovery() {
        bluetoothManager.startDiscovery()
    }

    fun stopDiscovery() {
        bluetoothManager.stopDiscovery()
    }

    fun connectToDevice(deviceAddress: String) {
        bluetoothManager.connectToDevice(deviceAddress)
    }

    fun disconnect() {
        bluetoothManager.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        stopDiscovery()
        bluetoothManager.cleanup()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BluetoothViewModel::class.java)) {
                val bluetoothManager = (application as StockApplication).bluetoothManager
                @Suppress("UNCHECKED_CAST")
                return BluetoothViewModel(bluetoothManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}