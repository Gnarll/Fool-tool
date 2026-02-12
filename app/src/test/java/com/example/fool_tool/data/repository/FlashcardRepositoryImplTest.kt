package com.example.fool_tool.data.repository

import app.cash.turbine.test
import com.example.fool_tool.data.local.dao.FlashcardDao
import com.example.fool_tool.data.mappers.toFlashcard
import com.example.fool_tool.data.mappers.toFlashcardEntity
import com.example.fool_tool.data.model.FlashcardEntity
import com.example.fool_tool.domain.model.factory.FlashcardFactory
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull


class FlashcardRepositoryImplTest() {
    val flashcardEntities: List<FlashcardEntity> = listOf(
        FlashcardFactory.createFlashcardEntity(id = 1),
        FlashcardFactory.createFlashcardEntity(id = 2),
        FlashcardFactory.createFlashcardEntity(id = 3)
    )

    lateinit var flashcardDao: FlashcardDao
    lateinit var repository: FlashcardRepositoryImpl


    @Before
    fun setUp() {
        flashcardDao = mockk<FlashcardDao>()
        repository = FlashcardRepositoryImpl(flashcardDao)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `repository gets all flashcards successfully`() = runTest {
        val firstEmit = flashcardEntities.take(3)
        val firstExpectedResult = firstEmit.map { it.toFlashcard() }

        val secondEmit = flashcardEntities.take(2)
        val secondExpectedResult = secondEmit.map { it.toFlashcard() }

        every { flashcardDao.getAll() } returns flow {
            emit(firstEmit)
            delay(100L)
            emit(secondEmit)
        }

        repository.getAllFlashcardsStream().test {
            assertEquals(awaitItem(), firstExpectedResult)
            assertEquals(awaitItem(), secondExpectedResult)
            awaitComplete()
        }

        verify(exactly = 1) { flashcardDao.getAll() }
    }

    @Test
    fun `repository inserts flashcard successfully`() = runTest {
        val slot = slot<FlashcardEntity>()
        coEvery { flashcardDao.insert(capture(slot)) } just runs

        val id = 1L

        repository.insertFlashcard(FlashcardFactory.createFlashcard(id = id))

        assertEquals(id, slot.captured.uid)

        coVerify(exactly = 1) { flashcardDao.insert(slot.captured) }
    }

    @Test
    fun `repository updates flashcard successfully`() = runTest {
        val slot = slot<FlashcardEntity>()

        coEvery { flashcardDao.update(capture(slot)) } just runs

        repository.updateFlashcard(FlashcardFactory.createFlashcard())

        coVerify { flashcardDao.update(slot.captured) }
    }

    @Test
    fun `repository deletes flashcard successfully`() = runTest {
        val slot = slot<FlashcardEntity>()

        coEvery { flashcardDao.delete(capture(slot)) } just runs

        repository.deleteFlashcard(FlashcardFactory.createFlashcard())

        coVerify { flashcardDao.delete(slot.captured) }
    }

    @Test
    fun `repository deletes flashcard by id flashcard successfully`() = runTest {
        val slot = slot<Long>()

        coEvery { flashcardDao.deleteById(capture(slot)) } just runs

        repository.deleteFlashcardById(1L)

        coVerify { flashcardDao.deleteById(slot.captured) }
    }

    @Test
    fun `repository reflects the db changes while it mutates`() = runTest {
        val dbStateFlow = MutableStateFlow(flashcardEntities.take(2))

        val targetFlashcardId = dbStateFlow.value.first().uid
        val targetFlashcard = FlashcardFactory.createFlashcard(
            id = targetFlashcardId,
            nativeWord = "new native",
            foreignWord = "new foreign",
        )
        val expectedFlashcardEntity = targetFlashcard.toFlashcardEntity()

        coEvery { flashcardDao.update(any<FlashcardEntity>()) } answers {
            val arg = firstArg<FlashcardEntity>()

            dbStateFlow.update { list ->
                list.map {
                    if (it.uid == arg.uid) {
                        arg
                    } else it
                }
            }
        }

        repository.updateFlashcard(targetFlashcard)

        assertNotNull(dbStateFlow.value.find { it.uid == expectedFlashcardEntity.uid })


        coEvery { flashcardDao.deleteById(any<Long>()) } answers {
            val arg = firstArg<Long>()

            dbStateFlow.update { list ->
                list.filter { it.uid != arg }
            }
        }

        repository.deleteFlashcardById(targetFlashcardId)

        assertNull(dbStateFlow.value.find { it.uid == expectedFlashcardEntity.uid })

        coEvery { flashcardDao.insert(any<FlashcardEntity>()) } answers {
            val arg = firstArg<FlashcardEntity>()

            dbStateFlow.update { list ->
                list + arg
            }
        }

        repository.insertFlashcard(targetFlashcard)

        assertNotNull(dbStateFlow.value.find { it.uid == expectedFlashcardEntity.uid })

        coVerify(exactly = 1) { flashcardDao.update(expectedFlashcardEntity) }
        coVerify(exactly = 1) { flashcardDao.deleteById(expectedFlashcardEntity.uid) }
        coVerify(exactly = 1) { flashcardDao.insert(expectedFlashcardEntity) }
    }
}