package com.example.fool_tool.data.use_cases

import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.ui.model.Reminder

interface RemindersProcessingUseCase {
    suspend fun reactivateOrDeclinePendingReminders()
    suspend fun cancelReminder(reminder: Reminder)
    suspend fun activateReminder(reminder: Reminder): ScheduleResult

    suspend fun deleteReminder(id: Long)
}