package com.example.fool_tool.data.repositories

import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.entities.toFlashcard
import com.example.fool_tool.ui.model.Flashcard
import com.example.fool_tool.ui.model.toFlashcardEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


interface FlashcardRepository {
    fun getAllFlashcardsStream(): Flow<List<Flashcard>>
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)
}

class FlashcardRepositoryImpl @Inject constructor(val flashcardDao: FlashcardDao) :
    FlashcardRepository {
    override fun getAllFlashcardsStream(): Flow<List<Flashcard>> =
        flashcardDao.getAll().map { flashcards ->
            flashcards.map { it.toFlashcard() }
        }

    override suspend fun insertFlashcard(flashcard: Flashcard) =
        flashcardDao.insert(flashcard.toFlashcardEntity())

    override suspend fun updateFlashcard(flashcard: Flashcard) =
        flashcardDao.update(flashcard.toFlashcardEntity())

    override suspend fun deleteFlashcard(flashcard: Flashcard) =
        flashcardDao.delete(flashcard.toFlashcardEntity())

}


