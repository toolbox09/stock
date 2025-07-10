package com.example.stock.test

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class BluetoothViewModel : ViewModel() {
    private var bluetoothAdapter: BluetoothAdapter? = null

    fun setBluetoothAdapter(adapter: BluetoothAdapter?) {
        bluetoothAdapter = adapter
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun getPairedDevices(): List<BluetoothDevice> {
        return (bluetoothAdapter?.bondedDevices?.toList() ?: emptyList())
    }
}

@SuppressLint("ServiceCast")
@Composable
fun BluetoothScreen2() {

    val context = LocalContext.current
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter = bluetoothManager.adapter
    val viewModel: BluetoothViewModel = viewModel()

    LaunchedEffect(Unit) {
        viewModel.setBluetoothAdapter(bluetoothAdapter)
    }


}
