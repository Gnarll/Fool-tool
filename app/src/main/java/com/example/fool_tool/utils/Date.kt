package com.example.fool_tool.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset


fun LocalDateTime.toMillisWithZone(): Long = atZone(ZoneId.systemDefault()).toEpochSecond() * 1000


fun Long.toUtcLocalDate(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneOffset.UTC)
    .toLocalDate()

fun Long.toLocalDateWithZone(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()