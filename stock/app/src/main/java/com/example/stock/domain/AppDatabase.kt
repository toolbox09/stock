package com.example.stock.domain

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.stock.domain.daos.*
import com.example.stock.domain.entities.*



@Database(
    entities = [
        UserEntity::class,
        WorkEntity::class,
        MasterEntity::class,
        LocationEntity::class,
        ScanItemEntity::class,
        BluetoothDeviceEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun workDao(): WorkDao
    abstract fun masterDao(): MasterDao
    abstract fun locationDao(): LocationDao
    abstract fun scanItemDao(): ScanItemDao
    abstract fun bluetoothDeviceDao(): BluetoothDeviceDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}