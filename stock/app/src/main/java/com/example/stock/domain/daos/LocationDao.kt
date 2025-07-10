package com.example.stock.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.stock.domain.entities.LocationEntity
import com.example.stock.entities.LocationInfo
import com.example.stock.entities.LocationKey
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insertLocation(location: LocationEntity) : Long

    @Query("SELECT * FROM location_entity WHERE id = :locationId")
    suspend fun getLocationById(locationId: Long): LocationEntity?

    @Query("DELETE FROM location_entity WHERE id = :id")
    suspend fun deleteLocationById(id: Long)

    @Query("DELETE FROM location_entity WHERE id IN (:ids)")
    suspend fun deleteLocationByIds(ids: List<Long>): Int

    @Query("DELETE FROM location_entity WHERE workId = 1")
    suspend fun deleteLocations()

    @Query("""
        UPDATE location_entity
        SET division = :division,
            name = :name
        WHERE id = :locationID
    """)
    suspend fun updateLocation( locationID: Long, division: String, name: String)

    @Query("SELECT name FROM location_entity WHERE workId = 1 AND division = :division")
    suspend fun getNamesByDivision(division: String): List<String>

    @Query("""
        SELECT 
            l.id AS id,
            l.workId AS workId,
            l.division AS division,
            l.name AS name,
            COALESCE(SUM(s.count), 0) AS count,
            l.createdTime AS createdTime
        FROM location_entity l
        LEFT JOIN scan_item_entity s ON l.id = s.locationId
        WHERE l.workId = 1
        GROUP BY l.id
    """)
    fun getLocationInfosFlow(): Flow<List<LocationInfo>>

    @Query("SELECT * FROM location_entity WHERE division = :division AND name = :name LIMIT 1")
    suspend fun getByDivisionAndName(division: String, name: String): LocationEntity?

    @Query("""
        SELECT id, division, name
        FROM location_entity 
        WHERE workId = 1
        ORDER BY division, name
    """)
    suspend fun getLocationKeys(): List<LocationKey>

    @Query("""
        SELECT id, division, name
        FROM location_entity 
        WHERE workId = 1 AND id = :locationId
        LIMIT 1
    """)
    suspend fun getLocationKeyByLocationId( locationId : Long ): LocationKey?

    @Query("""
        SELECT id FROM location_entity
        WHERE 
            (division > (SELECT division FROM location_entity WHERE id = :currentId)) OR
            (division = (SELECT division FROM location_entity WHERE id = :currentId) AND name > (SELECT name FROM location_entity WHERE id = :currentId))
        ORDER BY division ASC, name ASC
        LIMIT 1
    """)
    suspend fun getNextLocationId(currentId: Long): Long?

    @Query("""
        SELECT id FROM location_entity
        WHERE 
            (division < (SELECT division FROM location_entity WHERE id = :currentId)) OR
            (division = (SELECT division FROM location_entity WHERE id = :currentId) AND name < (SELECT name FROM location_entity WHERE id = :currentId))
        ORDER BY division DESC, name DESC
        LIMIT 1
    """)
    suspend fun getPreviousLocationId(currentId: Long): Long?
}