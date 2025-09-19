package com.example.fool_tool.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fool_tool.data.local.entities.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder")
    fun getAll(): Flow<List<ReminderEntity>>

    @Insert
    suspend fun insert(flashcard: ReminderEntity)

    @Update
    suspend fun update(reminderEntity: ReminderEntity)


    @Delete
    suspend fun delete(reminderEntity: ReminderEntity)
}