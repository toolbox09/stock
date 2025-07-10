package com.example.stock.screens.ble

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice as AndroidBluetoothDevice
import android.bluetooth.BluetoothManager as AndroidBluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stock.StockApplication
import com.example.stock.screens.bluetooth.BluetoothDevice
import com.example.stock.screens.bluetooth.BluetoothState
import com.example.stock.screens.bluetooth.BluetoothViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID


enum class BleState {
    DISABLED,    // 비활성화
    READY,       // 준비 완료
    SEARCHING,   // 검색 중
    CONNECTING, // 연결 시도 중 상태 추가
    CONNECTED,  // 연결 완료 상태 추가
    ERROR        // 오류 발생
}

class BleManager(private val context: Context) {

    val SPP_UUID: UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    val bleAdapter: BluetoothAdapter? by lazy {
        val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as AndroidBluetoothManager
        manager.adapter
    }

    private val _state = MutableStateFlow(BleState.DISABLED)
    val state: StateFlow<BleState> = _state.asStateFlow()

    private val _devices = MutableStateFlow<List<AndroidBluetoothDevice>>(emptyList())
    val devices: StateFlow<List<AndroidBluetoothDevice>> = _devices.asStateFlow()

    @SuppressLint("MissingPermission")
    fun refreshDevices() {
        val newDeviceList = bleAdapter?.bondedDevices?.toList() ?: emptyList()
        _devices.value = newDeviceList
    }


}

class BleViewModel(val bleManager : BleManager ) {

    val devices = bleManager.devices

    init {
        bleManager.refreshDevices()
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BluetoothViewModel::class.java)) {
                val bleManager = (application as StockApplication).bleManager
                @Suppress("UNCHECKED_CAST")
                return BleViewModel(bleManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}