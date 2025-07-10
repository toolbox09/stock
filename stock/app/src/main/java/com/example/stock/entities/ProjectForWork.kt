package com.example.stock.entities

data class ProjectForWork(
    val id: String,
    val projectName: String,
    val master: Map<String, String>? = null
)