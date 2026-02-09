package com.example.fool_tool.data.repository

import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.mappers.toFlashcard
import com.example.fool_tool.data.mappers.toFlashcardEntity
import com.example.fool_tool.domain.model.Flashcard
import com.example.fool_tool.domain.repository.FlashcardRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


@ActivityRetainedScoped
class FlashcardRepositoryImpl @Inject constructor(private val flashcardDao: FlashcardDao) :
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


