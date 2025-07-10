package com.example.stock.screens.bluetooth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stock.domain.entities.BluetoothDeviceEntity
import com.example.stock.domain.repositories.BluetoothDeviceRepository
import com.example.stock.screens.location.LocationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class BluetoothUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val devices : List<BluetoothDeviceEntity>? = null
)

class BluetoothViewModel2(
    private val repository: BluetoothDeviceRepository
) : ViewModel() {
    private val _state = MutableStateFlow(BluetoothUiState())
    val state: StateFlow<BluetoothUiState> = _state.asStateFlow()

    fun refresh(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)

        }
    }
}