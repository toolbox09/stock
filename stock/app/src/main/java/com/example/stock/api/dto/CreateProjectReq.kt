package com.example.stock.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateProjectReq(

    @SerialName("name")
    val name : String,

    @SerialName("masterUrl")
    val masterUrl : String?,

    @SerialName("matchUrl")
    val matchUrl : String?,
)