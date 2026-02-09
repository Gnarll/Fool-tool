package com.example.fool_tool.core_android.alarm

import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ScheduleResult

interface AlarmScheduler {
    fun schedule(reminder: Reminder): ScheduleResult
    fun cancel(reminderId: Long)

    fun checkIsAlarmPermissionGranted(): Boolean

    fun openAlarmPermissionSettings()
}