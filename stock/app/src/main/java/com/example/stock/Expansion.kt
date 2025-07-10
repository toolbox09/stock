package com.example.stock


import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Long.toDateString(): String =
    SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(this))