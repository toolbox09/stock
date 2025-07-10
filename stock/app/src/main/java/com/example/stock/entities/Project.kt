package com.example.stock.entities

data class Project(
    val id: String,
    val name: String,
    val created: String,
    val masterUrl: String?,
    val matchUrl: String?,
    val match: FileInfo?,
    val merge: FileInfo?,
    val works: List<FileInfo>
)