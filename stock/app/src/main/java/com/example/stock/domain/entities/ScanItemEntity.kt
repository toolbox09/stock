package com.example.stock.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "scan_item_entity",
    foreignKeys = [ForeignKey(
        entity = LocationEntity::class,
        parentColumns = ["id"],
        childColumns = ["locationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["locationId"])]
)
data class ScanItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val locationId: Long,
    val barcode: String,
    val masterName: String,
    val count: Int,
    val isMatch: Boolean,
    val isUpload: Boolean,
    val createdTime: Long = System.currentTimeMillis()
)
