package com.example.fool_tool.core.extensions

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDateTime.toMillisWithZone(): Long = atZone(ZoneId.systemDefault()).toEpochSecond() * 1000


fun LocalDateTime.toFormattedDetailedString(): String {
    val formatter = DateTimeFormatter.ofPattern(
        "EEE, HH:mm, dd MMM yyyy"
    )
    return this.format(formatter)
}