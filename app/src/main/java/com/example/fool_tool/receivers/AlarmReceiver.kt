package com.example.fool_tool.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.core_android.extensions.goAsync
import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.di.qualifiers.ExtraReminder
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ReminderStatus
import com.example.fool_tool.domain.repository.ReminderRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var reminderRepository: ReminderRepository

    @Inject
    lateinit var notificationService: NotificationService

    @Inject
    @ExtraReminder
    lateinit var extraReminder: String

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context != null && intent != null) {
            val serializedReminder = intent.getStringExtra(extraReminder)

            serializedReminder?.let {
                val reminder = Json.decodeFromString(Reminder.serializer(), it)

                notificationService.sendReminderNotification(reminder)
                reminderRepository.updateReminder(reminder.copy(status = ReminderStatus.SUCCEED))

            }

        }

    }
}