package com.example.fool_tool.data.use_cases

import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.ReminderStatus
import javax.inject.Inject

class RemindersProcessingUseCaseImpl @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) : RemindersProcessingUseCase {


    override suspend fun reactivateOrDeclinePendingReminders() {
        val pendingReminders = reminderRepository.getPendingReminders()

        pendingReminders.forEach { reminder ->
            val scheduleResult = alarmScheduler.schedule(reminder)

            when (scheduleResult) {
                is ScheduleResult.Success -> {

                }

                is ScheduleResult.FailedWithInvalidTime, is ScheduleResult.FailedWithNoPermission -> {
                    reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.DENIED))
                }
            }

        }
    }
}