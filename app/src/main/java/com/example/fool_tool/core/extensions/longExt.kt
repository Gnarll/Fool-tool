package com.example.fool_tool.core.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId


fun Long.toLocalDateWithZone(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun Long.toLocalDateTimeWithZone(): LocalDateTime = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDateTime()