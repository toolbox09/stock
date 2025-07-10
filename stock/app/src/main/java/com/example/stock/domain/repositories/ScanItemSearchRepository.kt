package com.example.stock.domain.repositories

import com.example.stock.domain.daos.ScanItemDao

class ScanItemSearchRepository(
    private val scanItemDao: ScanItemDao
) {
    fun searchScanItemByBarcode(searchQuery : String) = scanItemDao.searchScanItems(searchQuery)
}