package com.example.stock.domain.repositories

import com.example.stock.domain.daos.LocationDao

class LocationSelectRepository(
    private val locationDao: LocationDao,
) {
    suspend fun getLocationKeys() = locationDao.getLocationKeys()
    suspend fun getLocationKeyByLocationId(locationId : Long) = locationDao.getLocationKeyByLocationId(locationId)
}