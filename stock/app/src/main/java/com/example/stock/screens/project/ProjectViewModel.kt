package com.example.stock.screens.project


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.*
import com.example.stock.entities.ProjectInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.stock.domain.repositories.*


data class ProjectState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val projects : List<ProjectInfo>? = null,
    val currentProject : ProjectInfo? = null,
)


class ProjectViewModel( private val repository : ProjectRepository ) : ViewModel() {
    private val _state = MutableStateFlow(ProjectState())
    val state: StateFlow<ProjectState> = _state.asStateFlow()

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val targetRepository = ProjectRepository()
                ProjectViewModel(targetRepository)
            }
        }
    }

    fun getProjects() {
        viewModelScope.launch {
            _state.value = _state.value.copy( isLoading = true )

            repository.getProjectInfos()
                .onSuccess { res ->
                    _state.value = _state.value.copy(
                        projects = res,
                        isLoading = false,
                        error = null,
                    )
                }
                .onFailure { ex ->
                    _state.value = _state.value.copy(
                        projects = null,
                        isLoading = false,
                        error = ex.message,
                    )
                }
        }
    }
}
