package com.example.stock.screens.project

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.stock.api.dto.CollectRes
import com.example.stock.domain.repositories.ProjectDetailRepository
import com.example.stock.entities.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProjectDetailState(
    val isLoading: Boolean = false,
    val error: String? = null,
    var project : Project? = null,
    var collectRes : CollectRes? = null,
)

class ProjectDetailViewModel(
    private val projectName: String,
    private val repository: ProjectDetailRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProjectDetailState())
    val state: StateFlow<ProjectDetailState> = _state.asStateFlow()

    fun refresh(){
        viewModelScope.launch {
            _state.value = _state.value.copy( isLoading = true )

            repository.getProject(projectName)
                .onSuccess { res ->

                    if(res == null){
                        _state.value = _state.value.copy(
                            project = null,
                            collectRes = null,
                            isLoading = false,
                            error = "프로젝트를 찾을수 없습니다.",
                        )
                    }else {
                        _state.value = _state.value.copy(
                            project = res,
                            collectRes = null,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
                .onFailure { ex ->
                    _state.value = _state.value.copy(
                        project = null,
                        collectRes = null,
                        isLoading = false,
                        error = ex.message,
                    )
                }
        }
    }

    fun collect(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            repository.collectWork(projectName)
                .onSuccess { res ->
                    if(res == null){
                        _state.value = _state.value.copy(
                            collectRes = null,
                            isLoading = false,
                            error = "작업을 수집할수 없습니다.",
                        )
                    } else {
                        _state.value = _state.value.copy(
                            collectRes = res,
                            isLoading = false,
                            error = null,
                        )
                    }
                }
                .onFailure { ex ->
                    _state.value = _state.value.copy(
                        project = null,
                        collectRes = null,
                        isLoading = false,
                        error = ex.message,
                    )
                }
        }
    }

    fun clearResults() {
        viewModelScope.launch {
            _state.update { it.copy( collectRes = null) }
        }
    }
}

fun projectDetailViewModelFactory(projectName: String): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        val application = this[APPLICATION_KEY] as Application
        val repository = ProjectDetailRepository()
        ProjectDetailViewModel(projectName, repository)
    }
}

