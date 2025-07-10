package com.example.stock.domain.repositories

import android.annotation.SuppressLint
import com.example.stock.api.ApiInstance
import com.example.stock.domain.daos.*
import com.example.stock.domain.entities.LocationEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class WorkRepository(
    private val workDao: WorkDao,
    private val locationDao : LocationDao,
) {
    private val api = ApiInstance.apiService
    private val nameMutex = Mutex()

    suspend fun getWork() = workDao.getWork()
    suspend fun getWorkInfo() = workDao.getWorkInfo()
    suspend fun deleteWork() = workDao.deleteWork()

    suspend fun updateLocation(locationId: Long, division: String, name: String) = locationDao.updateLocation(locationId, division, name)
    suspend fun deleteLocationById( locationId : Long ) = locationDao.deleteLocationById(locationId)
    suspend fun deleteLocationByIds(ids: List<Long>) = locationDao.deleteLocationByIds(ids)
    suspend fun deleteLocationAll() = locationDao.deleteLocations()

    suspend fun startWork( projectName : String ) : Result<Long> {
        return try {
            val projectForWork = api.getProjectForWork(projectName)
            if( projectForWork == null ) throw IllegalArgumentException("프로젝트가 존재하지 않습니다.")

            val workId = workDao.updateWork(projectForWork)
            if(workId == null ) throw IllegalArgumentException("이미 진행중 프로젝트 입니다.")
            Result.success(workId)
        }catch (e : Exception ) {
            Result.failure(e)
        }
    }

    @SuppressLint("DefaultLocale")
    suspend fun createLocationName(division: String, suffix: String): String {
        val existingNames = locationDao.getNamesByDivision(division)
        val numbers = existingNames.mapNotNull { name ->
            Regex("""(\d+)$suffix""").find(name)?.groupValues?.get(1)?.toIntOrNull()
        }
        val maxNumber = numbers.maxOrNull() ?: 0
        return String.format("%03d%s", maxNumber + 1, suffix)
    }

    suspend fun createNextLocation(division: String, suffix: String ) : Long {
        return nameMutex.withLock {
            val locationName = createLocationName(division, suffix)
            val location = LocationEntity(
                workId = 1,
                division = division,
                name = locationName
            )
            return locationDao.insertLocation(location)
        }
    }

    fun getLocationInfosFlow() = locationDao.getLocationInfosFlow()
}