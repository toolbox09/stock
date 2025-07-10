package com.example.stock.screens.location.widget

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.domain.AppDatabase
import com.example.stock.domain.repositories.LocationSelectRepository
import com.example.stock.entities.LocationKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class LocationUiSelectState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val locationKeys: List<LocationKey> = emptyList(),
    val currentLocationKey: LocationKey? = null,
    val isBottomSheetVisible: Boolean = false
)

class LocationSelectViewModel(
    private val locationId : Long,
    private val repository: LocationSelectRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(LocationUiSelectState())
    val state: StateFlow<LocationUiSelectState> = _state.asStateFlow()


    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val locationKeys = repository.getLocationKeys()
                val locationKey = repository.getLocationKeyByLocationId(locationId)

                _state.update {
                    it.copy(
                        locationKeys = locationKeys,
                        currentLocationKey = locationKey,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "알 수 없는 오류가 발생했습니다"
                    )
                }
            }
        }
    }

    fun selectLocation(locationKey: LocationKey) {
        _state.update {
            it.copy(
                currentLocationKey = locationKey,
                isBottomSheetVisible = false
            )
        }
    }

    fun showBottomSheet() {
        _state.update { it.copy(isBottomSheetVisible = true) }
    }

    fun hideBottomSheet() {
        _state.update { it.copy(isBottomSheetVisible = false) }
    }

    fun clearSelection() {
        _state.update { it.copy(currentLocationKey = null) }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

fun locationSelectViewModelFactory(  locationId : Long ): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val repository = LocationSelectRepository(
            database.locationDao(),
        )
        LocationSelectViewModel(locationId, repository)
    }
}