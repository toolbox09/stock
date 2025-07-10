package com.example.stock.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.stock.domain.entities.ScanItemEntity
import com.example.stock.entities.ScanItemDto
import com.example.stock.entities.ScanItemForSearch
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanItemDao {
    @Insert
    suspend fun insertScanItem(scanItem: ScanItemEntity)

    @Update
    suspend fun updateScanItem(scanItem: ScanItemEntity)

    @Query("""
        UPDATE scan_item_entity
        SET barcode = :barcode,
            count = :count
        WHERE id = :id
    """)
    suspend fun updateScanItem(id: Long, barcode: String, count: Int)

    @Query("DELETE FROM scan_item_entity WHERE id = :id")
    suspend fun deleteScanItemById(id: Long)

    @Query("DELETE FROM scan_item_entity WHERE id IN (:ids)")
    suspend fun deleteScanItemByIds(ids: List<Long>): Int

    @Query("DELETE FROM scan_item_entity WHERE locationId = :locationId")
    suspend fun deleteScanItemByLocationId(locationId: Long)

    @Query("SELECT * FROM scan_item_entity WHERE locationId = :locationId")
    fun getScanItemByLocationId(locationId: Long): Flow<List<ScanItemEntity>>

    @Query("SELECT * FROM scan_item_entity WHERE barcode LIKE '%' || :searchQuery || '%' ORDER BY createdTime ASC")
    fun searchScanItemByBarcode(searchQuery: String): Flow<List<ScanItemEntity>>

    @Query("""
        SELECT
            si.id, si.locationId, si.barcode, si.masterName, si.count,
            si.isMatch, si.isUpload, si.createdTime,
            le.division AS locationDivision,
            le.name AS locationName
        FROM
            scan_item_entity AS si
        INNER JOIN
            location_entity AS le ON si.locationId = le.id
        WHERE
            si.barcode LIKE '%' || :query || '%' OR si.masterName LIKE '%' || :query || '%'
    """)
    fun searchScanItems(query: String): Flow<List<ScanItemForSearch>>

    @Query("""
        SELECT 
            s.id as id,
            l.division as locationDivision,
            l.name as locationName,
            s.barcode as barcode,
            s.masterName as masterName,
            s.count as count,
            s.isMatch as isMatch,
            s.isUpload as isUpload,
            s.createdTime as createdTime
        FROM scan_item_entity s
        INNER JOIN location_entity l ON s.locationId = l.id
        WHERE l.id IN (:locationIds)
        ORDER BY s.createdTime ASC
    """)
    suspend fun getScanItemByLocationIds(locationIds: List<Long>): List<ScanItemDto>

    @Query("""
        SELECT 
            s.id as id,
            l.division as locationDivision,
            l.name as locationName,
            s.barcode as barcode,
            s.masterName as masterName,
            s.count as count,
            s.isMatch as isMatch,
            s.isUpload as isUpload,
            s.createdTime as createdTime
        FROM scan_item_entity s
        INNER JOIN location_entity l ON s.locationId = l.id
        ORDER BY s.createdTime ASC
    """)
    suspend fun getAllScanItemsWithLocation(): List<ScanItemDto>

    @Query("UPDATE scan_item_entity SET isUpload = 1 WHERE id IN (:ids)")
    suspend fun updateUploadStatusForIds(ids: List<Long>)
}