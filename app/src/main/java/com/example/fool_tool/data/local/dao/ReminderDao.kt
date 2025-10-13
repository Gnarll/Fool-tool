package com.example.fool_tool.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fool_tool.data.local.entities.ReminderEntity

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY date DESC")
    fun getPagingSource(): PagingSource<Int, ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminderEntity: ReminderEntity)

    @Update
    suspend fun update(reminderEntity: ReminderEntity)


    @Delete
    suspend fun delete(reminderEntity: ReminderEntity)
}