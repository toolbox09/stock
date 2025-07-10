package com.example.stock.screens.work.widget

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.stock.entities.LocationInfo


@Composable
fun TotalScanItemCount(locationInfos: List<LocationInfo>) {
    val totalCount = remember(locationInfos) {
        locationInfos.sumOf { it.count }
    }
    Text("총 : $totalCount 개")
}