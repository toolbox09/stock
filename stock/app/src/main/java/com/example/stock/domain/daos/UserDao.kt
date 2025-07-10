package com.example.stock.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.stock.domain.entities.UserEntity

@Dao
interface UserDao {
    @Query("SELECT * FROM user_entity LIMIT 1")
    suspend fun getCurrentUser(): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("DELETE FROM user_entity")
    suspend fun deleteUser()

    @Query("SELECT COUNT(*) > 0 FROM user_entity")
    suspend fun hasUser(): Boolean

    @Transaction
    suspend fun updateUser( user : UserEntity ) {
        deleteUser()
        insertUser(user)
    }
}