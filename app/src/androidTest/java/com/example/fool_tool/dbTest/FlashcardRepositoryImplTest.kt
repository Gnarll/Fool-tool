package com.example.fool_tool.dbTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import app.cash.turbine.test
import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.db.AppDatabase
import com.example.fool_tool.data.repository.FlashcardRepositoryImpl
import com.example.fool_tool.domain.repository.FlashcardRepository
import com.example.fool_tool.test.FlashcardFactory
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test


class FlashcardRepositoryImplTest {
    lateinit var db: AppDatabase
    lateinit var flashcardDao: FlashcardDao
    lateinit var flashcardRepository: FlashcardRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        flashcardDao = db.flashcardDao()
        flashcardRepository = FlashcardRepositoryImpl(flashcardDao = flashcardDao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun flashcardRepository_insertAndGetFlashcardById_flashcardIsValid() = runTest {
        val flashcard = FlashcardFactory.createFlashcard()
        flashcardRepository.insertFlashcard(flashcard)

        val fetchedFlashcard = flashcardRepository.getFlashcardById(flashcard.id)
        assertEquals(flashcard, fetchedFlashcard)
    }

    @Test
    fun flashcardRepository_insertAndGetAllFlashcards_flashcardsAreValid() = runTest {
        val flashcardOne = FlashcardFactory.createFlashcard()
        val flashcardTwo = FlashcardFactory.createFlashcard()

        flashcardRepository.insertFlashcard(flashcardOne)
        flashcardRepository.insertFlashcard(flashcardTwo)

        flashcardRepository.getAllFlashcardsStream().test {
            val flashcards = awaitItem()
            assertEquals(flashcards.size, 2)
            assertTrue(flashcardOne in flashcards)
            assertTrue(flashcardTwo in flashcards)
        }
    }

    @Test
    fun flashcardRepository_insertAndDeleteFlashcard_flashcardIsDeleted() = runTest {
        val flashcard = FlashcardFactory.createFlashcard()

        flashcardRepository.insertFlashcard(flashcard)
        val fetchedFlashcard = flashcardRepository.getFlashcardById(flashcard.id)
        assertEquals(fetchedFlashcard, flashcard)

        flashcardRepository.deleteFlashcard(fetchedFlashcard)
        flashcardRepository.getAllFlashcardsStream().test {
            val fetchedFlashcardsList = awaitItem()
            assertEquals(fetchedFlashcardsList.size, 0)
        }

    }

    @Test
    fun flashcardRepository_insertAndUpdateFlashcard_flashcardIsUpdated() = runTest {
        var flashcard = FlashcardFactory.createFlashcard()

        flashcardRepository.insertFlashcard(flashcard)
        flashcard = flashcard.copy(foreignWord = "Updated")
        flashcardRepository.updateFlashcard(flashcard)

        val fetchedFlashcard = flashcardRepository.getFlashcardById(flashcard.id)
        assertEquals(fetchedFlashcard, flashcard)
    }
}