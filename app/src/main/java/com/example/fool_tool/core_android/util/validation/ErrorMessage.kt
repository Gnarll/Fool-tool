package com.example.fool_tool.core_android.util.validation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

class ErrorMessage(
    @get:StringRes
    val messageResId: Int,
    vararg val params: Any
) {
    @Composable
    fun asString(): String = stringResource(messageResId, *params)

    fun asString(context: Context): String = context.getString(messageResId, *params)
}