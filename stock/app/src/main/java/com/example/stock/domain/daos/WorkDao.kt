package com.example.stock.domain.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.stock.domain.entities.*
import com.example.stock.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(work: WorkEntity): Long

    @Query("SELECT * FROM work_entity WHERE id = 1 LIMIT 1")
    suspend fun getWork() : WorkEntity?

    @Query("DELETE FROM work_entity WHERE id = 1")
    suspend fun deleteWork()

    @Query("SELECT EXISTS(SELECT 1 FROM work_entity WHERE projectId = :projectId)")
    suspend fun existsByProjectId( projectId : String): Boolean

    @Query("""
        SELECT 
            u.keyword as userKeyword,
            w.projectName as projectName
        FROM user_entity u
        CROSS JOIN work_entity w
        LIMIT 1
    """)
    suspend fun getWorkInfo(): WorkInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMasterAll(masters: List<MasterEntity>)

    @Transaction
    suspend fun updateWork( projectForWork : ProjectForWork ) : Long? {
        if( existsByProjectId( projectForWork.id ) ) return null

        deleteWork()
        val workId = insertWork( WorkEntity( projectId = projectForWork.id, projectName = projectForWork.projectName))
        val masterList = projectForWork.master?.map { (key, value) ->
            MasterEntity(
                masterKey = key,
                masterName = value,
                workId = workId,
                createdTime = System.currentTimeMillis()
            )
        } ?: emptyList()
        insertMasterAll(masterList)
        return workId
    }
}