package com.example.stock.domain.repositories

import android.annotation.SuppressLint
import com.example.stock.domain.daos.*
import com.example.stock.domain.entities.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class LocationRepository(
    private val userDao: UserDao,
    private val masterDao: MasterDao,
    private val locationDao: LocationDao,
    private val scanItemDao: ScanItemDao
) {
    private val nameMutex = Mutex()

    suspend fun getSuffix() = userDao.getCurrentUser()?.keyword
    suspend fun getLocationById(locationId : Long) = locationDao.getLocationById(locationId)
    suspend fun getNextLocationId( locationId : Long ) = locationDao.getNextLocationId(locationId)
    suspend fun getPreviousLocationId( locationId : Long ) = locationDao.getPreviousLocationId(locationId)

    fun getScanItemByLocationId(locationId : Long ) = scanItemDao.getScanItemByLocationId(locationId)
    suspend fun insertScanItem(scanItem: ScanItemEntity) = scanItemDao.insertScanItem(scanItem)
    suspend fun updateScanItem(scanItemId: Long, barcode: String, count: Int) = scanItemDao.updateScanItem(scanItemId,barcode, count)
    suspend fun deleteScanItemById(scanItemId: Long) = scanItemDao.deleteScanItemById(scanItemId)
    suspend fun deleteScanItemByIds(scanItemIds: List<Long>) = scanItemDao.deleteScanItemByIds(scanItemIds)
    suspend fun deleteScanItemByLocationId(locationId: Long) = scanItemDao.deleteScanItemByLocationId(locationId)

    suspend fun getMaster(masterKey : String) = masterDao.getMaster(masterKey)
    suspend fun getMasterCount() = masterDao.getCount()


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
}
