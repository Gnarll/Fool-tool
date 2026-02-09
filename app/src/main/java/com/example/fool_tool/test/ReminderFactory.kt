package com.example.fool_tool.test

import com.example.fool_tool.data.model.ReminderEntity
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ReminderStatus
import java.time.LocalDateTime
import java.util.UUID

object ReminderFactory {
    fun createReminderEntity(
        id: Long = UUID.randomUUID().mostSignificantBits,
        date: LocalDateTime = LocalDateTime.now(),
        title: String = "Reminder's title",
        description: String = "Reminder's description",
        status: ReminderStatus = ReminderStatus.PENDING,
    ): ReminderEntity =
        ReminderEntity(
            uid = id,
            date = date,
            title = title,
            description = description,
            status = status,
        )

    fun createReminder(
        id: Long = UUID.randomUUID().mostSignificantBits,
        date: LocalDateTime = LocalDateTime.now(),
        title: String = "Reminder's title",
        description: String = "Reminder's description",
        status: ReminderStatus = ReminderStatus.PENDING,
    ): Reminder =
        Reminder(
            id = id,
            date = date,
            title = title,
            description = description,
            status = status,
        )
}