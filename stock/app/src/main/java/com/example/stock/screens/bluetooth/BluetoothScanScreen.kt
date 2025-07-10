package com.example.stock.screens.bluetooth

import android.Manifest
import android.app.Application
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.stock.components.HeaderRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BluetoothScanScreen(
    modifier : Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    val viewModel: BluetoothViewModel = viewModel(
        factory = BluetoothViewModel.Factory(application)
    )

    val bluetoothState by viewModel.bluetoothState.collectAsStateWithLifecycle()
    val discoveredDevices by viewModel.discoveredDevices.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        if (permissions.values.all { it }) {
            viewModel.bluetoothManager.updateBluetoothState()
        }
    }

    LaunchedEffect(Unit) {
        val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.ACCESS_FINE_LOCATION)
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        permissionLauncher.launch(permissionsToRequest)
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. 상태 및 제어 카드
        StatusAndControlCard(
            bluetoothState = bluetoothState,
            onStartScan = { viewModel.startDiscovery() },
            onStopScan = { viewModel.stopDiscovery() },
            onDisconnect = { viewModel.disconnect() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2. 장치 목록
        DeviceList(
            devices = discoveredDevices,
            bluetoothState = bluetoothState,
            isInteractionEnabled = bluetoothState == BluetoothState.READY || bluetoothState == BluetoothState.SEARCHING,
            onDeviceClick = { device ->
                viewModel.connectToDevice(device.address)
            }
        )
    }
}


@Composable
private fun StatusAndControlCard(
    bluetoothState: BluetoothState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onDisconnect: () -> Unit
) {
    Surface (
        modifier = Modifier
            .fillMaxWidth()
        // .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            HeaderRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = "상태: ${
                        when (bluetoothState) {
                            BluetoothState.DISABLED -> "블루투스 꺼짐"
                            BluetoothState.READY -> "준비 완료"
                            BluetoothState.SEARCHING -> "검색 중..."
                            BluetoothState.CONNECTING -> "연결 중..." // 상태 추가
                            BluetoothState.CONNECTED -> "연결됨" // 상태 추가
                            BluetoothState.ERROR -> "오류 발생"
                        }
                    }",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                when (bluetoothState) {
                    BluetoothState.SEARCHING -> {
                        Button(
                            onClick = onStopScan,
                            modifier = Modifier.weight(1f).height(35.dp),
                            contentPadding = PaddingValues(
                                top = 6.dp,
                                bottom = 6.dp
                            )
                        ) { Text("스캔 중지") }
                    }
                    BluetoothState.READY -> {
                        Button(
                            onClick = onStartScan,
                            modifier = Modifier.weight(1f).height(35.dp),
                            contentPadding = PaddingValues(
                                top = 6.dp,
                                bottom = 6.dp
                            )

                        ) { Text("스캔 시작") }
                    }
                    BluetoothState.CONNECTED -> {
                        Button(
                            onClick = onDisconnect,
                            modifier = Modifier.weight(1f).height(35.dp),
                            contentPadding = PaddingValues(
                                top = 6.dp,
                                bottom = 6.dp
                            )
                        ) {
                            Text("연결 해제")
                        }
                    }
                    BluetoothState.DISABLED -> {}
                    BluetoothState.CONNECTING -> {}
                    BluetoothState.ERROR -> {}
                }
            }
        }
    }
}

@Composable
private fun DeviceList(
    devices: List<BluetoothDevice>,
    bluetoothState: BluetoothState,
    isInteractionEnabled: Boolean, // 상위 상태에 따라 클릭 가능 여부 제어
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    Column {
        Text(
            text = "검색된 장치 (${devices.size})",
            // style = MaterialTheme.typography.titleLarge,
            fontSize = 15.sp,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        AnimatedVisibility(visible = bluetoothState == BluetoothState.SEARCHING) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(devices, key = { it.address }) { device ->
                DeviceItem(
                    device = device,
                    isEnabled = isInteractionEnabled, // 클릭 가능 여부 전달
                    onDeviceClick = onDeviceClick
                )
            }
        }
    }
}


@Composable
private fun DeviceItem(
    device: BluetoothDevice,
    isEnabled: Boolean,
    onDeviceClick: (BluetoothDevice) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEnabled && !device.isConnecting) { onDeviceClick(device) }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = device.name ?: "알 수 없는 장치",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = device.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (device.isConnecting) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }
    }
}

