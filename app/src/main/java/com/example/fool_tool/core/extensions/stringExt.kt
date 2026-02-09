package com.example.fool_tool.core.extensions

fun String.toShortNotificationContentText(symbolsCount: Int = 20) =
    take(symbolsCount) + if (this.length > symbolsCount) "..." else ""