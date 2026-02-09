package com.example.fool_tool.core_android.util

import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

fun polarToCart(
    degrees: Float,
    distance: Float,
    origin: Offset = Offset.Companion.Zero
): Offset = Offset(
    x = distance * cos(degrees * (PI / 180)).toFloat(),
    y = distance * -(sin(degrees * (PI / 180)).toFloat()),
) + origin