package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.repository.ReminderRepository
import javax.inject.Inject

class CancelReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(reminder: Reminder) {
        alarmScheduler.cancel(reminder.id)
        reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.CANCELLED))
    }
}