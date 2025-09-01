package com.example.fool_tool.ui.utils

import androidx.compose.ui.draw.CacheDrawScope
import androidx.compose.ui.geometry.Offset
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.cos
import kotlin.math.sin

fun polarToCart(
    degrees: Float,
    distance: Float,
    origin: Offset = Offset.Zero
): Offset = Offset(
    x = distance * cos(degrees * (PI / 180)).toFloat(),
    y = distance * -(sin(degrees * (PI / 180)).toFloat()),
) + origin

fun CacheDrawScope.calculateDistanceToRectBorder(angleDegrees: Float): Float {
    val angleRad = Math.toRadians(angleDegrees.toDouble()).toFloat()

    val halfWidth = size.width / 2
    val halfHeight = size.height / 2

    val dx = cos(angleRad).absoluteValue
    val lengthToRight = if (dx == 0f) Float.POSITIVE_INFINITY else halfWidth / dx

    val dy = sin(angleRad).absoluteValue
    val lengthToTop = if (dy == 0f) Float.POSITIVE_INFINITY else halfHeight / dy

    return minOf(lengthToRight, lengthToTop)
}