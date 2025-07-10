package com.example.stock.screens.work

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.domain.AppDatabase
import com.example.stock.domain.entities.ScanItemEntity
import com.example.stock.domain.repositories.ScanItemSearchRepository
import com.example.stock.entities.ScanItemForSearch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class ScanItemSearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val scanItems : List<ScanItemForSearch> = emptyList(),
)

class ScanItemSearchViewModel(
    private val repository: ScanItemSearchRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ScanItemSearchUiState())
    val state: StateFlow<ScanItemSearchUiState> = _state.asStateFlow()

    fun search( query : String ) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            try {

                repository.searchScanItemByBarcode(query)
                    .onEach { scanItems ->
                        _state.update { it.copy(scanItems = scanItems) }
                    }.launchIn(viewModelScope)

                _state.update { it.copy(
                    isLoading = false,
                    error = null
                ) }

            }catch (e : Exception){
                _state.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }
}


fun scanItemSearchViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val repository = ScanItemSearchRepository(
            database.scanItemDao()
        )
        ScanItemSearchViewModel(repository)
    }
}
