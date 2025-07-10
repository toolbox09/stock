package com.example.stock.domain.repositories

import android.util.Log
import com.example.stock.api.ApiInstance
import com.example.stock.api.dto.*
import com.example.stock.domain.daos.ScanItemDao
import com.example.stock.domain.daos.WorkDao
import com.example.stock.entities.ScanItemForUpload
import com.example.stock.entities.*

class ScanItemUploadRepository(
    private val workDao: WorkDao,
    private val scanItemDao: ScanItemDao
) {
    private val apiService = ApiInstance.apiService

    suspend fun getWorkInfo() = workDao.getWorkInfo()

    suspend fun getScanItemsByLocationIds(locationIds: List<Long>): List<ScanItemForUpload> {
        val dtoList = scanItemDao.getScanItemByLocationIds(locationIds)
        return dtoList.map { dto -> dto.toScanItemForUpload() }
    }

    suspend fun getAllScanItems(): List<ScanItemForUpload> {
        val dtoList = scanItemDao.getAllScanItemsWithLocation()
        return dtoList.map { dto -> dto.toScanItemForUpload() }
    }

    suspend fun appendWork( req : AppendWorkReq ) : Result<List<Long>> {
        return try {
            val result = apiService.appendWork(req)

            if( result.isNullOrEmpty() ) {
                throw IllegalArgumentException("업로드 에 실패하였습니다.")
            }

            scanItemDao.updateUploadStatusForIds(result)
            Result.success(result)
        } catch ( e : Exception ) {
            Result.failure(e)
        }
    }
}
