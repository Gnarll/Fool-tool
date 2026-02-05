package com.example.fool_tool.data.use_cases

import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import javax.inject.Inject

class RemindersProcessingUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) : RemindersProcessingUseCase {

    override suspend fun cancelReminder(reminder: Reminder) {
        alarmScheduler.cancel(reminder.id)
        reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.CANCELLED))
    }

    override suspend fun deleteReminder(id: Long) {
        alarmScheduler.cancel(id)
        reminderRepository.deleteReminder(id)
    }

    override suspend fun activateReminder(reminder: Reminder): ScheduleResult {
        val scheduleResult = alarmScheduler.schedule(reminder)

        if (scheduleResult is ScheduleResult.Success) {
            reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.PENDING))
        }

        return scheduleResult
    }

    override suspend fun reactivateOrDeclinePendingReminders() {
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