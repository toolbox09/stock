package com.example.stock.api.dto

import com.example.stock.entities.ScanItemForUpload
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppendWorkReq(
    @SerializedName("projectName")
    val projectName: String,

    @SerializedName("fileName")
    val fileName: String,

    @SerialName("scanItems")
    val scanItems: List<ScanItemForUpload>
)