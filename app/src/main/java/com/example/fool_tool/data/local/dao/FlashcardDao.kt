package com.example.fool_tool.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fool_tool.data.local.entities.FlashcardEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FlashcardDao {
    @Query("SELECT * FROM flashcard")
    fun getAll(): Flow<List<FlashcardEntity>>

    @Query(
        "SELECT * FROM flashcard" +
                " WHERE flashcard.uid = :id"
    )
    suspend fun getById(id: Long): FlashcardEntity

    @Insert
    suspend fun insert(flashcard: FlashcardEntity)

    @Update
    suspend fun update(flashcard: FlashcardEntity)

    @Delete
    suspend fun delete(flashcard: FlashcardEntity)

    @Query("DELETE from flashcard WHERE uid = :id")
    suspend fun deleteById(id: Long)
}