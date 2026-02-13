package com.example.fool_tool.core_android.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.fool_tool.core.extensions.toLocalDateTimeWithZone
import com.example.fool_tool.core.extensions.toMillisWithZone
import com.example.fool_tool.domain.model.ScheduleResult
import com.example.fool_tool.domain.model.factory.ReminderFactory
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.spyk
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class AlarmSchedulerImplTest {
    @MockK
    lateinit var context: Context

    @MockK
    lateinit var alarmManager: AlarmManager

    lateinit var alarmSchedulerImpl: AlarmSchedulerImpl

    private val extraReminder = "extra_reminder"


    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        alarmSchedulerImpl = AlarmSchedulerImpl(context = context, extraReminder = extraReminder)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `scheduling returns with fail by permission when check fails`() {
        alarmSchedulerImpl = spyk(alarmSchedulerImpl)

        every { alarmSchedulerImpl.checkIsAlarmPermissionGranted() } returns false

        val reminder = ReminderFactory.createReminder()
        val scheduleResult = alarmSchedulerImpl.schedule(reminder)

        assertEquals(scheduleResult, ScheduleResult.FailedWithNoPermission)
        verify(exactly = 1) { alarmSchedulerImpl.checkIsAlarmPermissionGranted() }
    }

    @Test
    fun `scheduling returns with fail by invalid time set`() {
        val invalidTimeInMillis = System.currentTimeMillis() - 1000L
        val invalidDate = invalidTimeInMillis.toLocalDateTimeWithZone()

        alarmSchedulerImpl = spyk(alarmSchedulerImpl)

        mockkConstructor(Intent::class)
        every {
            anyConstructed<Intent>().putExtra(any<String>(), any<String>())
        } returns mockk()

        every { alarmSchedulerImpl.checkIsAlarmPermissionGranted() } returns true

        val reminder = ReminderFactory.createReminder(date = invalidDate)
        val scheduleResult = alarmSchedulerImpl.schedule(reminder)

        assertEquals(scheduleResult, ScheduleResult.FailedWithInvalidTime)
        verify(exactly = 1) { alarmSchedulerImpl.checkIsAlarmPermissionGranted() }
        verify(exactly = 1) { anyConstructed<Intent>().putExtra(extraReminder, any<String>()) }
    }

    @Test
    fun `scheduling returns with success`() {
        val timeInMillis = System.currentTimeMillis() + 10_000L
        val date = timeInMillis.toLocalDateTimeWithZone()

        alarmSchedulerImpl = spyk(alarmSchedulerImpl)

        mockkConstructor(Intent::class)
        every {
            anyConstructed<Intent>().putExtra(any<String>(), any<String>())
        } returns mockk()

        mockkStatic(PendingIntent::class)
        every { PendingIntent.getBroadcast(any(), any(), any(), any()) } returns mockk()

        every { alarmSchedulerImpl.checkIsAlarmPermissionGranted() } returns true

        every { alarmManager.setExactAndAllowWhileIdle(any(), any(), any()) } just runs

        val reminder = ReminderFactory.createReminder(date = date)
        val scheduleResult = alarmSchedulerImpl.schedule(reminder)

        assertEquals(scheduleResult, ScheduleResult.Success)
        verify(exactly = 1) { alarmSchedulerImpl.checkIsAlarmPermissionGranted() }
        verify(exactly = 1) {
            alarmManager.setExactAndAllowWhileIdle(
                any(),
                reminder.date.toMillisWithZone(),
                any()
            )
        }
        verify(exactly = 1) {
            PendingIntent.getBroadcast(
                any(),
                reminder.id.hashCode(),
                any(),
                any()
            )
        }
        verify(exactly = 1) { anyConstructed<Intent>().putExtra(extraReminder, any<String>()) }
    }

    @Test
    fun `alarm cancellation occurs with proper request code`() {
        mockkStatic(PendingIntent::class)
        every { PendingIntent.getBroadcast(any(), any(), any(), any()) } returns mockk()

        every { alarmManager.cancel(any<PendingIntent>()) } just runs

        val reminder = ReminderFactory.createReminder()

        alarmSchedulerImpl.cancel(reminder.id)

        verify(exactly = 1) { alarmManager.cancel(any<PendingIntent>()) }
        verify(exactly = 1) {
            PendingIntent.getBroadcast(
                any(),
                reminder.id.hashCode(),
                any(),
                any()
            )
        }
    }
}