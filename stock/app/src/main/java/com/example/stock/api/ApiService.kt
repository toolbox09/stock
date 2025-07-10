package com.example.stock.api

import com.example.stock.api.dto.*
import com.example.stock.entities.*
import retrofit2.http.*

interface ApiService {
    @GET("login")
    suspend fun login(@Query("id") id: String, @Query("password") password: String) : LoginRes

    @GET("project")
    suspend fun getProjectInfos() : List<ProjectInfo>

    @GET("project_for_work/{projectName}")
    suspend fun getProjectForWork(@Path("projectName") projectName: String): ProjectForWork?

    @POST("work/append")
    suspend fun appendWork(@Body req : AppendWorkReq) : List<Long>?

    @POST("project")
    suspend fun createProject(@Body req : CreateProjectReq) : Int

    @GET("master")
    suspend fun getMasterList(): List<FileInfo>

    @GET("match")
    suspend fun getMatchList(): List<FileInfo>

    @GET("project/{projectName}")
    suspend fun getProject(@Path("projectName") projectName: String) : Project?

    @GET("work/collect/{projectName}")
    suspend fun collectWork(@Path("projectName") projectName: String) : CollectRes?
}