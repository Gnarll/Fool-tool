package com.example.fool_tool.utils

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter


fun LocalDateTime.toMillisWithZone(): Long = atZone(ZoneId.systemDefault()).toEpochSecond() * 1000

fun LocalDate.toMillisWithZone(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toUtcLocalDate(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneOffset.UTC)
    .toLocalDate()

fun Long.toLocalDateWithZone(): LocalDate = Instant.ofEpochMilli(this)
    .atZone(ZoneId.systemDefault())
    .toLocalDate()

fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}


fun LocalDateTime.toFormattedDetailedString(): String {
    val formatter = DateTimeFormatter.ofPattern(
        "EEE, HH:mm, dd MMM yyyy"
    )
    return this.format(formatter)
}

