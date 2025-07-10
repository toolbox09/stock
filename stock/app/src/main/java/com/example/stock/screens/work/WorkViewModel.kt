package com.example.stock.screens.work

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.domain.AppDatabase
import kotlinx.coroutines.flow.*

import com.example.stock.domain.entities.WorkEntity
import com.example.stock.domain.repositories.WorkRepository
import com.example.stock.entities.LocationInfo
import com.example.stock.entities.WorkInfo
import kotlinx.coroutines.launch

data class WorkUiState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val workInfo : WorkInfo? = null,
    val locationInfos : List<LocationInfo> = emptyList(),
    val createLocationId : Long? = null,
)

class WorkViewModel( private val repository: WorkRepository ) : ViewModel() {
    private val _state = MutableStateFlow(WorkUiState())
    val state: StateFlow<WorkUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val workInfo = repository.getWorkInfo()
                if (workInfo == null) {
                    _state.update { it.copy(workInfo = null, error = null, isLoading = false) }
                    return@launch
                }

                _state.update { it.copy(workInfo = workInfo, isLoading = false, error = null) }
                repository.getLocationInfosFlow()
                    .onEach {  locationInfos ->
                        _state.update {  it.copy( locationInfos = locationInfos )  }
                    }
                    .launchIn(viewModelScope)
            }catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun open(projectName : String) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }


            repository.startWork(projectName)
                .onSuccess {
                    refresh()
                    val workInfo = repository.getWorkInfo()
                     if(workInfo != null) {
                        createNextLocation("가", workInfo.userKeyword, true )
                    }

                }
                .onFailure {
                    _state.update { it.copy(isLoading = false, error = it.error) }
                }
        }
    }

    fun close() {
        viewModelScope.launch {
            repository.deleteWork()
            refresh()
        }
    }

    fun deleteLocationById(locationId: Long) {
        viewModelScope.launch {
            repository.deleteLocationById(locationId)
        }
    }

    fun deleteLocationByIds( locationIds : List<Long> ) {
        viewModelScope.launch {
            repository.deleteLocationByIds(locationIds)
        }
    }

    fun deleteLocationAll() {
        viewModelScope.launch {
            repository.deleteLocationAll()
        }
    }

    fun updateLocation(id: Long, division: String, name: String) {
        viewModelScope.launch {
            repository.updateLocation(id, division, name)
        }
    }

    fun createNextLocation(division: String, suffix: String, isStart : Boolean = false ) {
        viewModelScope.launch {
            val locationId = repository.createNextLocation(division, suffix)
            if(isStart) {
                _state.update { it.copy(createLocationId = locationId) }
            }
        }
    }

    fun clearCreateLocationId() {
        _state.update { it.copy(createLocationId = null) }
    }
}

fun workViewModelFactory() :  ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val repository = WorkRepository(
            database.workDao(),
            database.locationDao(),
        )
        WorkViewModel(repository)
    }
}