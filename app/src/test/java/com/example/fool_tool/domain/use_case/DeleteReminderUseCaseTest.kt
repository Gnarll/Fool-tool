package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.factory.ReminderFactory
import com.example.fool_tool.domain.repository.ReminderRepository
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class DeleteReminderUseCaseTest {

    @MockK
    lateinit var reminderRepository: ReminderRepository

    @MockK
    lateinit var alarmScheduler: AlarmScheduler

    lateinit var useCase: DeleteReminderUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = DeleteReminderUseCase(reminderRepository, alarmScheduler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `use case cancels reminder and deletes it`() = runTest {
        coEvery { alarmScheduler.cancel(any()) } just runs
        coEvery { reminderRepository.deleteReminder(any()) } just runs

        val reminder = ReminderFactory.createReminder()

        useCase(reminder.id)

        coVerify(exactly = 1) { alarmScheduler.cancel(reminder.id) }
        coVerify(exactly = 1) { reminderRepository.deleteReminder(reminder.id) }
    }
}