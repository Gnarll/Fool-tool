package com.example.fool_tool.domain.use_case

import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteReminderUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(id: Long) {
        alarmScheduler.cancel(id)
        reminderRepository.deleteReminder(id)
    }
}