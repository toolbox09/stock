package com.example.stock.entities

import com.example.stock.domain.entities.ScanItemEntity

data class BarcodeMergeItem(
    val barcode: String,
    val masterName: String?,
    val totalCount: Int
)

fun mergeScanItem(items: List<ScanItemEntity>): List<ScanItemEntity> {
    return items
        .groupBy { item -> "${item.barcode}|${item.masterName}" }

        // 2. 각 그룹 처리
        .map { (_, group) ->
            ScanItemEntity(
                id = group.first().id,
                locationId = group.first().locationId,
                barcode = group.first().barcode,
                masterName = group.first().masterName,
                isMatch = group.first().isMatch,
                isUpload = false,
                count = group.sumOf { it.count },
                createdTime = group.last().createdTime
            )
        }
}
