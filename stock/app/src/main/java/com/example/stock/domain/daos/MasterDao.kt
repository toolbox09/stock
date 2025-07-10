package com.example.stock.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.stock.domain.entities.MasterEntity
import com.example.stock.entities.MasterKeyValue
import kotlinx.coroutines.flow.Flow

@Dao
interface MasterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(masters: List<MasterEntity>)

    @Query("SELECT * FROM master_entity WHERE masterKey = :masterKey LIMIT 1")
    suspend fun getMaster( masterKey : String ): MasterEntity?

    @Query("SELECT COUNT(*) FROM master_entity WHERE workId = 1")
    suspend fun getCount(): Int

    @Query("SELECT * FROM master_entity WHERE workId = 1")
    fun getMasterFlow(): Flow<List<MasterEntity>>

    @Query("DELETE FROM master_entity WHERE workId = 1")
    suspend fun deleteAll()

    @Query("SELECT masterKey, masterName FROM master_entity WHERE workId = 1")
    suspend fun getMasters(): List<MasterKeyValue>
}