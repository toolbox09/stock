package com.example.stock.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun LocationName(
    division : String,
    name : String,
) {
    Row {
        Text(division)
        Text(". ${name}")
    }
}