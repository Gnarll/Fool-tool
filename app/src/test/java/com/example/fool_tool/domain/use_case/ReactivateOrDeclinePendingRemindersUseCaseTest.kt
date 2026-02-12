package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.Reminder
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

class ReactivateOrDeclinePendingRemindersUseCaseTest {

    @MockK
    lateinit var reminderRepository: ReminderRepository

    @MockK
    lateinit var alarmScheduler: AlarmScheduler

    lateinit var useCase: ReactivateOrDeclinePendingRemindersUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = ReactivateOrDeclinePendingRemindersUseCase(reminderRepository, alarmScheduler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `use case tries to schedule reminders and update their status accordingly`() = runTest {
        val reminders = (1L..10L).map { ReminderFactory.createReminder(id = it) }

        coEvery { reminderRepository.getPendingReminders() } returns reminders
        coEvery { alarmScheduler.schedule(any()) } answers {
            val reminder = firstArg<Reminder>()
            val result = getSuccessResultForEvenAndFailForOddId(reminder.id)

            result
        }
        coEvery { reminderRepository.updateReminder(any()) } just runs

        useCase()

        reminders.forEach {
            val scheduleResult = getSuccessResultForEvenAndFailForOddId(it.id)

            val reminderStatus =
                if (scheduleResult is ScheduleResult.Success) ReminderStatus.PENDING else ReminderStatus.DENIED

            coVerify { alarmScheduler.schedule(it) }
            coVerify { reminderRepository.updateReminder(it.copy(status = reminderStatus)) }
        }

        coVerify(exactly = 1) { reminderRepository.getPendingReminders() }
    }

    private fun getSuccessResultForEvenAndFailForOddId(id: Long): ScheduleResult {
        return if ((id and 1) == 0L)
            ScheduleResult.Success
        else ScheduleResult.FailedWithNoPermission
    }
}