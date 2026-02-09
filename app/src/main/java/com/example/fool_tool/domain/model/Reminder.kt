package com.example.fool_tool.domain.model

import androidx.compose.runtime.Immutable
import com.example.fool_tool.core.serializers.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
@Immutable
data class Reminder(
    val id: Long,
    @Serializable(with = LocalDateTimeSerializer::class)
    val date: LocalDateTime,
    val title: String,
    val description: String,
    val status: ReminderStatus
)


