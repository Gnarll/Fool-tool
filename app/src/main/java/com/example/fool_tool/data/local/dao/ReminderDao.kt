package com.example.fool_tool.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fool_tool.data.local.entities.ReminderEntity
import com.example.fool_tool.ui.model.ReminderStatus

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY date DESC")
    fun getPagingSource(): PagingSource<Int, ReminderEntity>

    @Query("SELECT * FROM reminder WHERE status = :status ")
    fun getRemindersByStatus(status: ReminderStatus): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminderEntity: ReminderEntity)

    @Update
    suspend fun update(reminderEntity: ReminderEntity)


    @Query("DELETE FROM reminder WHERE uid = :id")
    suspend fun delete(id: Long)
}