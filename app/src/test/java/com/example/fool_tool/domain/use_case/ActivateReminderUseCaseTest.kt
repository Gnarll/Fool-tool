package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.ScheduleResult
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
import kotlin.test.assertEquals

class ActivateReminderUseCaseTest {
    @MockK
    lateinit var reminderRepository: ReminderRepository

    @MockK
    lateinit var alarmScheduler: AlarmScheduler

    lateinit var useCase: ActivateReminderUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ActivateReminderUseCase(reminderRepository, alarmScheduler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `use case successfully schedules reminder and updates it's status to pending`() = runTest {
        coEvery { alarmScheduler.schedule(any()) } returns ScheduleResult.Success
        coEvery { reminderRepository.updateReminder(any()) } just runs

        val reminder = ReminderFactory.createReminder()

        val result = useCase(reminder)

        assertEquals(result, ScheduleResult.Success)

        coVerify(exactly = 1) { alarmScheduler.schedule(reminder.copy(status = ReminderStatus.PENDING)) }
        coVerify(exactly = 1) { reminderRepository.updateReminder(reminder) }
    }

    @Test
    fun `use case fails to schedule reminder and doesn't update it`() = runTest {
        coEvery { alarmScheduler.schedule(any()) } returns ScheduleResult.FailedWithNoPermission
        coEvery { reminderRepository.updateReminder(any()) } just runs

        val reminder = ReminderFactory.createReminder()

        val result = useCase(reminder)

        assertEquals(result, ScheduleResult.FailedWithNoPermission)

        coVerify(exactly = 1) { alarmScheduler.schedule(reminder) }
        coVerify(exactly = 0) { reminderRepository.updateReminder(any()) }
    }

}