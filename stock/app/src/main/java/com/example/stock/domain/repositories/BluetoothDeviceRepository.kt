package com.example.stock.domain.repositories

import com.example.stock.domain.daos.BluetoothDeviceDao
import com.example.stock.domain.entities.BluetoothDeviceEntity
import com.example.stock.screens.bluetooth.BluetoothDevice
import kotlinx.coroutines.flow.Flow

class BluetoothDeviceRepository(
    private val bluetoothDeviceDao: BluetoothDeviceDao
) {
    val allDevices: Flow<List<BluetoothDeviceEntity>> = bluetoothDeviceDao.getAllDevices()

    suspend fun addOrUpdateDevice(device: BluetoothDevice) {
        val entity = BluetoothDeviceEntity(
            address = device.address,
            name = device.name ?: "Unknown",
            lastConnectedTime = System.currentTimeMillis()
        )
        bluetoothDeviceDao.insert(entity)
    }

    suspend fun updateConnectionTime(address: String) {
        bluetoothDeviceDao.updateConnectionTime(address, System.currentTimeMillis())
    }

    suspend fun removeDevice(address: String) {
        bluetoothDeviceDao.delete(BluetoothDeviceEntity(address, "", 0))
    }
}