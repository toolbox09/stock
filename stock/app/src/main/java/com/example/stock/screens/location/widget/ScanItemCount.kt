package com.example.stock.screens.location.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.stock.domain.entities.ScanItemEntity

@Composable
fun ScanItemCount(scanItems: List<ScanItemEntity> ) {
    val totalCount = remember(scanItems) {
        scanItems.sumOf { it.count }
    }

    Text("$totalCount 개")
}
