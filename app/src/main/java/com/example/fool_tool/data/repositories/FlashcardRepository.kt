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
    suspend fun getFlashcardById(id: Long): Flashcard
    suspend fun insertFlashcard(flashcard: Flashcard)
    suspend fun updateFlashcard(flashcard: Flashcard)
    suspend fun deleteFlashcard(flashcard: Flashcard)

    suspend fun deleteFlashcardById(id: Long)
}

class FlashcardRepositoryImpl @Inject constructor(val flashcardDao: FlashcardDao) :
    FlashcardRepository {
    override suspend fun getFlashcardById(id: Long): Flashcard =
        flashcardDao.getById(id = id).toFlashcard()


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

    override suspend fun deleteFlashcardById(id: Long) {
        flashcardDao.deleteById(id)
    }
}


