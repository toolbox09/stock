package com.example.stock.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "location_entity",
    foreignKeys = [ForeignKey(
        entity = WorkEntity::class,
        parentColumns = ["id"],
        childColumns = ["workId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["workId"])]
)
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val workId: Long,
    val division: String,
    val name: String,
    val createdTime: Long = System.currentTimeMillis()
)
