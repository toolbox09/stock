package com.example.stock.screens.work

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.api.dto.AppendWorkReq
import com.example.stock.domain.AppDatabase
import com.example.stock.domain.repositories.ScanItemUploadRepository
import com.example.stock.entities.ScanItemForUpload
import com.example.stock.entities.WorkInfo
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


data class ScanItemUploadUiState(
    var locationIds : List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val scanItems : List<ScanItemForUpload> = emptyList(),
    var isAllowReUpload : Boolean = false,
    val workInfo : WorkInfo? = null,
    val results : List<Long>? = null,
)

class ScanItemUploadViewModel(
    private val locationIds: List<Long>,
    private val repository: ScanItemUploadRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ScanItemUploadUiState( locationIds = locationIds ))
    val state: StateFlow<ScanItemUploadUiState> = _state.asStateFlow()

    fun refreshForIds(locationIds : List<Long>) {
        _state.update { it.copy( locationIds = locationIds ) }
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {
                val scanItems = if( _state.value.locationIds.isEmpty()) repository.getAllScanItems() else repository.getScanItemsByLocationIds(_state.value.locationIds)
                if ( scanItems.isEmpty() ) {
                    _state.update { it.copy(isLoading = false, error = "대상이 존재하지 않습니다.") }
                    return@launch
                }

                val workInfo =  repository.getWorkInfo()
                if ( workInfo == null ){
                    _state.update { it.copy(isLoading = false, error = "작업정보 오류가 있습니다..") }
                    return@launch
                }

                _state.update {
                    it.copy(
                        scanItems = if( _state.value.isAllowReUpload ) scanItems else scanItems.filter {  !it.isUpload  },
                        workInfo = workInfo,
                        isLoading = false,
                        error = null
                    )
                }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun toggleAllowReUpload(){
        viewModelScope.launch {
            _state.update { it.copy( isAllowReUpload = !_state.value.isAllowReUpload ) }
        }
        refresh()
    }

    fun clearResults() {
        viewModelScope.launch {
            _state.update { it.copy( results = null) }
        }
    }


    fun appendWork() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, results = null) }
            try {
                val workInfo = _state.value.workInfo
                val scanItems = _state.value.scanItems


                workInfo?.let { info ->
                    repository.appendWork( AppendWorkReq(
                        projectName = info.projectName,
                        fileName = info.userKeyword,
                        scanItems = scanItems
                    ))
                        .onSuccess { res ->
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = null,
                                results = res,
                            )
                        }
                        .onFailure { ex ->
                            _state.value = _state.value.copy(
                                isLoading = false,
                                error = ex.message,
                                results = null,
                            )
                        }
                }

            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message, results = null ) }
            }
        }
    }
}

fun scanItemUploadViewModelFactory(locationIds: List<Long>): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val repository = ScanItemUploadRepository(
            database.workDao(),
            database.scanItemDao()
        )
        ScanItemUploadViewModel(locationIds, repository)
    }
}


