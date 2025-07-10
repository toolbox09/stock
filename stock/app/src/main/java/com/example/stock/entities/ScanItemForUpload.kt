package com.example.stock.entities

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class ScanItemForUpload(
    val id: Long,
    val locationDivision: String,
    val locationName: String,
    val barcode: String,
    val masterName: String,
    val count: Int,
    val isMatch: Boolean,
    val isUpload: Boolean,
    val date: String,
    val time: String
)