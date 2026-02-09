package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.ScheduleResult
import com.example.fool_tool.domain.repository.ReminderRepository
import javax.inject.Inject

class ReactivateOrDeclinePendingRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke() {
        val pendingReminders = reminderRepository.getPendingReminders()

        pendingReminders.forEach { reminder ->
            val scheduleResult = alarmScheduler.schedule(reminder)

            when (scheduleResult) {
                is ScheduleResult.Success -> {
                    reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.PENDING))
                }

                is ScheduleResult.FailedWithInvalidTime, is ScheduleResult.FailedWithNoPermission -> {
                    reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.DENIED))
                }
            }

        }
    }
}