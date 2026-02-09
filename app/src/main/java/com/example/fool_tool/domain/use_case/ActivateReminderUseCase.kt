package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.model.ScheduleResult
import com.example.fool_tool.domain.repository.ReminderRepository
import javax.inject.Inject

class ActivateReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(reminder: Reminder): ScheduleResult {
        val scheduleResult = alarmScheduler.schedule(reminder)

        if (scheduleResult is ScheduleResult.Success) {
            reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.PENDING))
        }

        return scheduleResult
    }
}