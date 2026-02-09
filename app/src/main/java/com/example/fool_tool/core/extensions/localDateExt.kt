package com.example.fool_tool.core.extensions

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun LocalDate.toMillisWithZone(): Long =
    atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDate.toFormattedString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return this.format(formatter)
}