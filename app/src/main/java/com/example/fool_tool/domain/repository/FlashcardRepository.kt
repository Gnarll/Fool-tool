package com.example.fool_tool.domain.repository

import com.example.fool_tool.domain.model.Flashcard
import kotlinx.coroutines.flow.Flow

interface FlashcardRepository {
    fun getAllFlashcardsStream(): Flow<List<Flashcard>>
    suspend fun getFlashcardById(id: Long): Flashcard
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)

    suspend fun deleteFlashcardById(id: Long)
}