package com.example.fool_tool.dbTest

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.local.db.AppDatabase
import com.example.fool_tool.data.local.entities.FlashcardEntity
import com.example.fool_tool.ui.utils.FlashcardCreating
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch

@RunWith(AndroidJUnit4::class)
class FlashcardDaoTest {
    private lateinit var flashcardDao: FlashcardDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        flashcardDao = db.flashcardDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun db_writeAndReadFlashcard_flashcardIsValid() = runTest {
        val flashcardOne: FlashcardEntity =
            FlashcardCreating.createFlashcardEntity(nativeWord = "One")
        val flashcardTwo: FlashcardEntity =
            FlashcardCreating.createFlashcardEntity(nativeWord = "Two")


        flashcardDao.insert(flashcardOne)
        flashcardDao.insert(flashcardTwo)

        val latch = CountDownLatch(1)

        val job = launch(Dispatchers.IO) {
            flashcardDao.getAll().collect {
                assertEquals(it.size, 2)
                assertTrue(flashcardOne in it)
                assertTrue(flashcardTwo in it)

                latch.countDown()
            }
        }

        latch.await()
        job.cancelAndJoin()
    }

    @Test
    fun db_insertAndDeleteFlashcard_flashcardIsDeleted() = runTest {
        val flashcard = FlashcardCreating.createFlashcardEntity()

        flashcardDao.getAll().test {
            assertEquals(emptyList<FlashcardEntity>(), awaitItem())

            flashcardDao.insert(flashcard)
            assertEquals(listOf(flashcard), awaitItem())

            flashcardDao.delete(flashcard)
            assertEquals(emptyList<FlashcardEntity>(), awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun db_insertAndGetByIdFlashcard_flashcardIsValid() = runTest {
        val flashcard = FlashcardCreating.createFlashcardEntity()
        flashcardDao.insert(flashcard)

        val fetchedFlashcard = flashcardDao.getById(flashcard.uid)
        assertEquals(fetchedFlashcard, flashcard)
    }

    @Test
    fun db_addAndUpdateFlashcard_flashcardIsUpdated() = runTest {
        var flashcard = FlashcardCreating.createFlashcardEntity(nativeWord = "one")
        flashcardDao.insert(flashcard)

        flashcard = flashcard.copy(nativeWord = "two")
        flashcardDao.update(flashcard)
        val updatedFlashcard = flashcardDao.getById(flashcard.uid)

        assertEquals(updatedFlashcard, flashcard)
    }


}