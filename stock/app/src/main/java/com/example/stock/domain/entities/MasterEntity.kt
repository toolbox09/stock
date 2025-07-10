package com.example.stock.domain.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "master_entity",
    foreignKeys = [ForeignKey(
        entity = WorkEntity::class,
        parentColumns = ["id"],
        childColumns = ["workId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["workId"])]
)
data class MasterEntity(
    @PrimaryKey
    val masterKey: String,
    val masterName: String,
    val workId: Long,
    val createdTime: Long = System.currentTimeMillis()
)
