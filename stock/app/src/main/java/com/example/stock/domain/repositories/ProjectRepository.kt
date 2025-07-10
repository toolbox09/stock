package com.example.stock.domain.repositories

import com.example.stock.api.ApiInstance
import com.example.stock.entities.ProjectInfo

class ProjectRepository() {
    private val apiService = ApiInstance.apiService

    suspend fun getProjectInfos() : Result<List<ProjectInfo>> {
        return try {
            val projectInfos = apiService.getProjectInfos()
            Result.success(projectInfos)
         } catch ( e : Exception ) {
            Result.failure(e)
        }
    }
}