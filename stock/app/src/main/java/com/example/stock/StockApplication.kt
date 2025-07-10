package com.example.stock

import android.app.Application
import com.example.stock.screens.ble.BleManager
import com.example.stock.screens.bluetooth.BluetoothManager

class StockApplication : Application() {
    lateinit var bluetoothManager: BluetoothManager
        private set

    lateinit var bleManager: BleManager
        private set

    override fun onCreate() {
        super.onCreate()
        bluetoothManager = BluetoothManager(applicationContext)
        bleManager = BleManager(applicationContext)
    }
}