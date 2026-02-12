package com.example.fool_tool.data.repository

import androidx.paging.PagingConfig
import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.example.fool_tool.data.local.dao.ReminderDao
import com.example.fool_tool.data.mappers.toReminder
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.factory.ReminderFactory
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class ReminderRepositoryImplTest {
    @MockK
    lateinit var reminderDao: ReminderDao
    lateinit var repository: ReminderRepositoryImpl

    val reminderPagingConfig = PagingConfig(
        pageSize = 20,
        initialLoadSize = 40,
        prefetchDistance = 10,
        enablePlaceholders = false
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)


        repository = ReminderRepositoryImpl(reminderDao, reminderPagingConfig)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `repository gets reminder by id successfully`() = runTest {
        val reminderEntity = ReminderFactory.createReminderEntity()

        coEvery { reminderDao.getReminderById(reminderEntity.uid) } returns reminderEntity

        repository.getReminderById(reminderEntity.uid)

        coVerify(exactly = 1) { reminderDao.getReminderById(reminderEntity.uid) }
    }

    @Test
    fun `repository gets pending reminders successfully`() = runTest {
        val reminderEntities = listOf(
            ReminderFactory.createReminderEntity(),
            ReminderFactory.createReminderEntity(status = ReminderStatus.PENDING),
            ReminderFactory.createReminderEntity(status = ReminderStatus.PENDING)
        )

        val expectedReminders =
            reminderEntities.filter { it.status == ReminderStatus.PENDING }
                .map {
                    it.toReminder()
                }

        coEvery { reminderDao.getRemindersByStatus(any()) } answers {
            val argStatus = firstArg<ReminderStatus>()

            reminderEntities.filter {
                it.status == argStatus
            }
        }

        val resultReminders = repository.getPendingReminders()

        assertEquals(expectedReminders, resultReminders)

        coVerify(exactly = 1) { reminderDao.getRemindersByStatus(ReminderStatus.PENDING) }
    }

    @Test
    fun `repository inserts reminder successfully`() = runTest {
        val reminderEntity = ReminderFactory.createReminderEntity()
        val reminder = reminderEntity.toReminder()

        coEvery { reminderDao.insert(any()) } just runs

        repository.insertReminder(reminder)

        coVerify(exactly = 1) { reminderDao.insert(reminderEntity) }
    }

    @Test
    fun `repository gets correct reminders offset`() = runTest {
        val id = 1L
        val expectedOffset = 10

        coEvery { reminderDao.getReminderOffset(id) } returns expectedOffset

        val resultOffset = repository.getReminderOffset(id)

        assertEquals(expectedOffset, resultOffset)

        coVerify(exactly = 1) { reminderDao.getReminderOffset(id) }
    }

    @Test
    fun `repository updates reminder successfully`() = runTest {
        val reminderEntity = ReminderFactory.createReminderEntity()
        val reminder = reminderEntity.toReminder()

        coEvery { reminderDao.update(any()) } just runs

        repository.updateReminder(reminder)

        coVerify(exactly = 1) { reminderDao.update(reminderEntity) }
    }

    @Test
    fun `repository deletes reminder successfully`() = runTest {
        val id = 1L

        coEvery { reminderDao.delete(any()) } just runs

        repository.deleteReminder(id)

        coVerify(exactly = 1) { reminderDao.delete(id) }
    }

    @Test
    fun `repository provides with paged flashcards`() = runTest {

        val reminderEntities = (0L..100L).map { index ->
            ReminderFactory.createReminderEntity(id = index)
        }

        val pagingSourceFactory = reminderEntities.asPagingSourceFactory()

        every { reminderDao.getPagingSource() } answers {
            pagingSourceFactory.invoke()
        }


        val pagedRemindersFlow = repository.getPagedReminders()


        with(reminderPagingConfig) {
            val snapshot1 = pagedRemindersFlow.asSnapshot {
                scrollTo(initialLoadSize - prefetchDistance - 1)
            }

            assertEquals(snapshot1.size, initialLoadSize)

            val snapshot2 = pagedRemindersFlow.asSnapshot {
                scrollTo(initialLoadSize - prefetchDistance)
            }

            assertEquals(snapshot2.size, initialLoadSize + pageSize)

            val snapshot3 = pagedRemindersFlow.asSnapshot {
                scrollTo(initialLoadSize + pageSize)
            }

            val expectedSnapshot3Size = initialLoadSize + pageSize * 2
            assertEquals(snapshot3.size, expectedSnapshot3Size)

            assertEquals(snapshot3[0], reminderEntities[0].toReminder())
            assertEquals(
                snapshot3.last(),
                reminderEntities[expectedSnapshot3Size - 1].toReminder()
            )
        }
    }
}