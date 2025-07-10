package com.example.stock.entities

data class LocationInfo(
    val id: Long,
    val workId: Long,
    val division: String,
    val name: String,
    val count : Long,
    val createdTime: Long,
)
