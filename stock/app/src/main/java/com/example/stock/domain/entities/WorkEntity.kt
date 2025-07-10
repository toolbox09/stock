package com.example.stock.domain.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "work_entity",
    indices = [Index(value = ["projectId"], unique = true)]
)
data class WorkEntity(
    @PrimaryKey
    val id: Long = 1,
    val projectId: String,
    val projectName: String,
    val createdTime: Long = System.currentTimeMillis(),
    val updatedTime: Long = System.currentTimeMillis()
)