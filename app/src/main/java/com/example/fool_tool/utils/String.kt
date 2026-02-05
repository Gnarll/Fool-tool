package com.example.fool_tool.utils


fun String.toShortNotificationContentText(symbolsCount: Int = 20) =
    take(symbolsCount) + if (this.length > symbolsCount) "..." else ""
