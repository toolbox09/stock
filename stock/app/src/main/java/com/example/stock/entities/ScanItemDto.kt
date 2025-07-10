package com.example.stock.entities

data class ScanItemDto(
    val id: Long,
    val locationDivision: String,
    val locationName: String,
    val barcode: String,
    val masterName: String,
    val count: Int,
    val isMatch: Boolean,
    val isUpload: Boolean,
    val createdTime: Long
)

fun ScanItemDto.toScanItemForUpload(): ScanItemForUpload {
    val dateTime = java.util.Date(this.createdTime)
    val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
    val timeFormat = java.text.SimpleDateFormat("HH:mm:ss", java.util.Locale.getDefault())

    return ScanItemForUpload(
        id = this.id,
        locationDivision = this.locationDivision,
        locationName = this.locationName,
        barcode = this.barcode,
        masterName = this.masterName,
        count = this.count,
        isMatch = this.isMatch,
        isUpload = this.isUpload,
        date = dateFormat.format(dateTime),
        time = timeFormat.format(dateTime)
    )
}
