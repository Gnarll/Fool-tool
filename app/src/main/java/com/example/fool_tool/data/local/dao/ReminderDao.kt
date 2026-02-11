package com.example.fool_tool.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.fool_tool.data.model.ReminderEntity
import com.example.fool_tool.domain.model.ReminderStatus

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminder ORDER BY date DESC")
    fun getPagingSource(): PagingSource<Int, ReminderEntity>

    @Query("SELECT * FROM reminder")
    fun getAll(): List<ReminderEntity>

    @Query(
        """
    SELECT (
      SELECT COUNT(*)
      FROM reminder AS r2
      WHERE r2.date > r1.date
         OR (r2.date = r1.date AND r2.uid < r1.uid)
    ) AS rn
    FROM reminder AS r1
    WHERE r1.uid = :id
"""
    )
    suspend fun getReminderOffset(id: Long): Int?

    @Query("SELECT * FROM reminder WHERE uid = :id")
    suspend fun getReminderById(id: Long): ReminderEntity

    @Query("SELECT * FROM reminder WHERE status = :status ")
    fun getRemindersByStatus(status: ReminderStatus): List<ReminderEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reminderEntity: ReminderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMany(reminders: List<ReminderEntity>)

    @Update
    suspend fun update(reminderEntity: ReminderEntity)


    @Query("DELETE FROM reminder WHERE uid = :id")
    suspend fun delete(id: Long)
}