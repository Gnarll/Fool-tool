package com.example.fool_tool.core_android.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SdkSuppress
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertEquals

@RunWith(AndroidJUnit4::class)
class AlarmSchedulerImplTest {
    private val context = mockk<Context>(relaxed = true)

    private val alarmManager = mockk<AlarmManager>()

    private lateinit var alarmScheduler: AlarmSchedulerImpl
    private val extraReminder = "extra_reminder"

    @Before
    fun setUp() {
        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        alarmScheduler = AlarmSchedulerImpl(context, extraReminder)
    }

    @SdkSuppress(minSdkVersion = Build.VERSION_CODES.S)
    @Test
    fun verify_proper_check_for_permission_for_API_higher_than_S() {
        val canScheduleMock = false
        every { alarmManager.canScheduleExactAlarms() } returns canScheduleMock

        val isPermissionGrantedResult = alarmScheduler.checkIsAlarmPermissionGranted()

        verify(exactly = 1) { alarmManager.canScheduleExactAlarms() }
        assertEquals(canScheduleMock, isPermissionGrantedResult)
    }

    @SdkSuppress(maxSdkVersion = Build.VERSION_CODES.R)
    @Test
    fun verify_proper_check_for_permission_for_API_lower_than_S() {
        val isPermissionGrantedResult = alarmScheduler.checkIsAlarmPermissionGranted()

        assertEquals(true, isPermissionGrantedResult)
    }

    @Test
    fun verify_whether_settings_are_opened_for_different_API() {
        val intentSlot = slot<Intent>()

        every { context.startActivity(capture(intentSlot)) } just runs

        alarmScheduler.openAlarmPermissionSettings()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            verify(exactly = 1) { context.startActivity(intentSlot.captured) }
        } else {
            verify(exactly = 0) { context.startActivity(any()) }
        }
    }
}