package com.example.stock.domain.daos

import androidx.room.*
import com.example.stock.domain.entities.BluetoothDeviceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BluetoothDeviceDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(device: BluetoothDeviceEntity)

    @Delete
    suspend fun delete(device: BluetoothDeviceEntity)

    @Query("SELECT * FROM bluetooth_devices ORDER BY lastConnectedTime DESC")
    fun getAllDevices(): Flow<List<BluetoothDeviceEntity>>

    @Query("UPDATE bluetooth_devices SET lastConnectedTime = :timestamp WHERE address = :address")
    suspend fun updateConnectionTime(address: String, timestamp: Long)
}