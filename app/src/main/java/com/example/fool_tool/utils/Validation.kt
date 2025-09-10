package com.example.fool_tool.utils

import androidx.annotation.StringRes

interface ValidationError {
    @get:StringRes
    val messageResId: Int
}