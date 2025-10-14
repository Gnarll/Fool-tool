package com.example.fool_tool.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BootCompletedReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var alarmScheduler: AlarmScheduler

    @Inject
    lateinit var reminderRepository: ReminderRepository

    override fun onReceive(context: Context?, intent: Intent?) = goAsync { pendingResult ->
        if (context != null && intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                val pendingReminders = reminderRepository.getPendingReminders()

                pendingReminders.forEach { reminder ->
                    val result = alarmScheduler.schedule(reminder)

                    if (result is ScheduleResult.FailedWithInvalidTime) {
                        reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.DENIED))
                    }
                }

            }
        }
    }
}