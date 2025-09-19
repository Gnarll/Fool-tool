package com.example.fool_tool.data.local

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

class Converters {
    @TypeConverter
    fun fromLocalDateTime(localDateTime: LocalDateTime?): Long? {
        return localDateTime?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
    }

    @TypeConverter
    fun toLocalDateTime(timestamp: Long?): LocalDateTime? {
        return timestamp?.let {
            LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneId.systemDefault())
        }
    }

}