package com.example.stock.screens.work

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.domain.AppDatabase
import kotlinx.coroutines.flow.*

import com.example.stock.domain.repositories.ProjectRepository
import com.example.stock.domain.repositories.UserRepository
import com.example.stock.entities.ProjectInfo
import com.example.stock.screens.root.RootViewModel
import kotlinx.coroutines.launch


data class ProjectSelectUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val projectInfos : List<ProjectInfo> = emptyList(),
    val selectProjectInfo : ProjectInfo? = null,
)

class ProjectSelectViewModel(
    private val projectRepository : ProjectRepository
) : ViewModel() {
    private val _state = MutableStateFlow(ProjectSelectUiState())
    val state: StateFlow<ProjectSelectUiState> = _state.asStateFlow()

    fun refresh() {
        if(_state.value.isLoading) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            projectRepository.getProjectInfos()
                .onSuccess { res ->
                    _state.update { it.copy( isLoading = false, projectInfos = res ) }
                }
                .onFailure { e ->
                    _state.update { it.copy( isLoading = false, error = e.message ) }
                }
        }
    }

    fun setSelectProjectInfo( projectInfo : ProjectInfo? ) {
        viewModelScope.launch {
            _state.update {  it.copy( selectProjectInfo = projectInfo )  }
        }
    }
}

fun projectSelectViewModelFactory() : ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val repository = ProjectRepository()
        ProjectSelectViewModel(repository)
    }
}