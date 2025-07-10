package com.example.stock.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bluetooth_devices")
data class BluetoothDeviceEntity(
    @PrimaryKey val address: String,
    val name: String,
    val lastConnectedTime: Long = 0
)