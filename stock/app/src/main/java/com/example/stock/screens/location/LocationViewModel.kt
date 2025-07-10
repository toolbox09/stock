package com.example.stock.screens.location

import androidx.lifecycle.viewModelScope
import com.example.stock.domain.entities.*
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.domain.AppDatabase
import com.example.stock.domain.repositories.LocationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.find


data class LocationState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val suffix : String? = null,
    var location : LocationEntity? = null,
    val scanItems : List<ScanItemEntity> = emptyList(),
    val hasMaster : Boolean = false,
    // val master : Set<Pair<String, String>>? = null,
    val nextLocationId : Long? = null,
    val previousLocationId : Long? = null,
    val createLocationId : Long? = null,
)

class LocationViewModel(
    private val locationId: Long,
    private val repository: LocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LocationState())
    val state: StateFlow<LocationState> = _state.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val location = repository.getLocationById(locationId)
                if (location == null) {
                    _state.update { it.copy(isLoading = false, error = "작업을 찾을 수 없습니다.") }
                    return@launch
                }
                // _state.update { it.copy(location = location, isLoading = false, error = null) }

                repository.getScanItemByLocationId(locationId)
                    .onEach { scanItems ->
                        _state.update { it.copy(scanItems = scanItems) }
                    }
                    .launchIn(viewModelScope)

                // val master = repository.getMasters()
                val masterCount = repository.getMasterCount()
                val nextLocationId = repository.getNextLocationId(locationId)
                val previousLocationId = repository.getPreviousLocationId(locationId)
                val suffix = repository.getSuffix()
                _state.update {
                    it.copy(
                        location = location,
                        suffix = suffix,
                        hasMaster = masterCount > 0,
                        nextLocationId = nextLocationId,
                        previousLocationId = previousLocationId,
                        isLoading = false,
                        error = null
                    )
                }



            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun insertScanItem(scanItem: ScanItemEntity) {
        viewModelScope.launch {
            repository.insertScanItem(scanItem)
        }
    }

    fun updateScanItem(scanItemId: Long, barcode: String, count: Int) {
        viewModelScope.launch {
            repository.updateScanItem(scanItemId, barcode, count)
        }
    }

    fun deleteScanItemById(scanItemId: Long) {
        viewModelScope.launch {
            repository.deleteScanItemById(scanItemId)
        }
    }

    fun deleteScanItemByIds(scanItemIds: List<Long>) {
        viewModelScope.launch {
            repository.deleteScanItemByIds(scanItemIds)
        }
    }

    fun deleteScanItemByLocationId(locationId: Long) {
        viewModelScope.launch {
            repository.deleteScanItemByLocationId(locationId)
        }
    }

    suspend  fun validateBarcodeAndGetValue(barcode: String): Pair<Boolean, String?> {
        if (!_state.value.hasMaster) {
            return Pair(true, null)
        }

        val entity = repository.getMaster(barcode)
        return if (entity != null) {
            Pair(true, entity.masterName)
        } else {
            Pair(false, null)
        }
    }

    fun createNextLocation(division: String) {
        viewModelScope.launch {
            _state.value.suffix?.let {  suffix ->
                val locationId = repository.createNextLocation(division,  suffix)
                _state.update { it.copy( createLocationId = locationId ) }
            }
        }
    }

    fun clearCreateLocationId() {
        viewModelScope.launch {
            _state.update { it.copy( createLocationId = null ) }
        }
    }

}

fun locationViewModelFactory(locationId: Long): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val locationRepository = LocationRepository(
            database.userDao(),
            database.masterDao(),
            database.locationDao(),
            database.scanItemDao()
        )
        LocationViewModel(locationId, locationRepository)
    }
}
