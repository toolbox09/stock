package com.example.stock.domain.repositories

import com.example.stock.entities.User
import com.example.stock.api.ApiInstance
import com.example.stock.api.dto.LoginRes
import com.example.stock.domain.entities.UserEntity
import com.example.stock.domain.daos.UserDao

class UserRepository(private val userDao: UserDao) {
    private val apiService = ApiInstance.apiService

    suspend fun login( id : String, password : String ) : Result<User> {
        return try {
            val res = apiService.login( id, password )
            userDao.updateUser(UserEntity( res.id, res.keyword ))
            Result.success(User(res.id, res.keyword))
        }catch ( e : Exception ) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        userDao.deleteUser()
    }

    suspend fun getUser(): UserEntity? = userDao.getCurrentUser()
}