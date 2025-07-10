package com.example.stock.domain.repositories

import com.example.stock.api.ApiInstance
import com.example.stock.api.dto.CollectRes
import com.example.stock.entities.Project

class ProjectDetailRepository() {
    private val apiService = ApiInstance.apiService

    suspend fun getProject( projectName : String ) : Result<Project?> {
        return try {
            val project = apiService.getProject(projectName)
            Result.success(project)
        } catch ( e : Exception ) {
            Result.failure(e)
        }
    }

    suspend fun collectWork( projectName : String ) : Result<CollectRes?> {
        return try {
            val project = apiService.collectWork(projectName)
            Result.success(project)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}