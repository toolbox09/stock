package com.example.stock.screens.root

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

import com.example.stock.domain.AppDatabase
import com.example.stock.domain.repositories.UserRepository
import com.example.stock.entities.User



data class RootUiState(
    val isLoading: Boolean = false,
    val error : String? = null,
    val user : User? = null,
)

class RootViewModel( private val uerRepository : UserRepository  ) :  ViewModel() {
    private val _state = MutableStateFlow(RootUiState())
    val state: StateFlow<RootUiState> = _state.asStateFlow()

    init { initUser() }

    fun initUser() {
        if( _state.value.user != null ) return;
        viewModelScope.launch {
            val user = uerRepository.getUser()
            if( user != null ){
                _state.update { it.copy( isLoading = false, error = null, user = User(user.id, user.keyword ))  }
            }
        }
    }

    fun login(id: String, password: String) {
        if(_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            uerRepository.login(id, password)
                .onSuccess { user ->
                    _state.update { it.copy( isLoading = false, user = user )  }
                }
                .onFailure { e ->
                    _state.update { it.copy( isLoading = false, error = e.message ) }
                }
        }
    }

    fun logout() {
        viewModelScope.launch {
            uerRepository.logout()
            _state.update { it.copy( isLoading = false, user = null, error = null ) }
        }
    }

    fun clearError() {
        _state.update { it.copy(error = null) }
    }
}

fun rootViewModelFactory() : ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val database = AppDatabase.getDatabase(application)
        val repository = UserRepository(
            database.userDao()
        )
        RootViewModel(repository)
    }
}