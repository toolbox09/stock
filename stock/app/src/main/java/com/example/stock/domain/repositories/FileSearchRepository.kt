package com.example.stock.domain.repositories

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.*
import com.example.stock.api.ApiInstance
import com.example.stock.api.dto.CreateProjectReq
import com.example.stock.entities.FileInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FileSearchRepository() {
    private val apiService = ApiInstance.apiService


    suspend fun createProject(req: CreateProjectReq): Result<Int> {
        return try {
            withContext(Dispatchers.IO) {
                val response = apiService.createProject(req)
                Result.success(response)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMasterList() : Result<List<FileInfo>> {
        return try {
            val projects = apiService.getMasterList()
            Result.success(projects)
        } catch ( e : Exception ) {
            Result.failure(e)
        }
    }

    suspend fun getMatchList() : Result<List<FileInfo>> {
        return try {
            val projects = apiService.getMatchList()
            Result.success(projects)
        } catch ( e : Exception ) {
            Result.failure(e)
        }
    }
}
