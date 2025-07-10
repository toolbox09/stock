package com.example.stock.entities

data class ScanItemForSearch(
    val id: Long,
    val locationId: Long,
    val barcode: String,
    val masterName: String,
    val count: Int,
    val isMatch: Boolean,
    val isUpload: Boolean,
    val createdTime: Long,
    val locationDivision : String,
    val locationName : String,
)
