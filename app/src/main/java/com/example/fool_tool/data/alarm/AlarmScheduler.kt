package com.example.fool_tool.data.alarm

import com.example.fool_tool.ui.model.Reminder

sealed interface ScheduleResult {
    object Success : ScheduleResult
    object FailedWithNoPermission : ScheduleResult
    object FailedWithInvalidTime : ScheduleResult

}

interface AlarmScheduler {
    fun schedule(reminder: Reminder): ScheduleResult
    fun cancel(reminderId: Long)

    fun checkIsAlarmPermissionGranted(): Boolean
}