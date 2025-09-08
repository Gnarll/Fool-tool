package com.example.fool_tool.ui.utils

import androidx.annotation.StringRes

interface ValidationError {
    @get:StringRes
    val messageResId: Int
}