package com.example.fool_tool.ui.model

import com.example.fool_tool.data.local.entities.ReminderEntity
import java.time.LocalDateTime

enum class ReminderStatus {
    PENDING,
    SUCCEED,

    DENIED
}

data class Reminder(
    val id: Long,
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val status: ReminderStatus
)


fun Reminder.toReminderEntity() =
    ReminderEntity(uid = id, date = date, title = title, description = description, status = status)