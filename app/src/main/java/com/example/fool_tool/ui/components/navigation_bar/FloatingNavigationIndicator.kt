package com.example.fool_tool.ui.components.navigation_bar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.ClipOp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.util.lerp
import com.example.fool_tool.core_android.extensions.calculateDistanceToRectBorder
import com.example.fool_tool.core_android.util.polarToCart
import kotlin.math.hypot

@Composable
fun FloatingNavigationIndicator(
    shapeProgressProvider: () -> Float,
    color: Color,
    numberOfWaves: Int = 5,
    modifier: Modifier = Modifier
) {
    Spacer(
        modifier = modifier.drawWithCache {
            val origin = Offset(size.width / 2, size.height / 2)
            val circleSizeCoefficient = 1f / 4f
            val circleRadius = minOf(size.width, size.height) * circleSizeCoefficient
            val clipperInitialRadius = hypot(size.width / 2, size.height / 2)
            val angleStep = 360f / numberOfWaves

            val clipPath = Path()
            val blotPaths = List(numberOfWaves) { Path() }
            val blotCircles = MutableList(numberOfWaves) { Offset.Zero to 0f }

            onDrawBehind {
                val clipperCurrentRadius =
                    lerp(clipperInitialRadius, circleRadius, 1 - shapeProgressProvider())

                clipPath.reset()
                clipPath.addOval(Rect(center = origin, radius = clipperCurrentRadius))

                repeat(numberOfWaves) { i ->
                    val midAngle = i * angleStep
                    val leftAngle = midAngle + (angleStep / 2)
                    val rightAngle = midAngle - (angleStep / 2)

                    val midRadius = calculateDistanceToRectBorder(midAngle)
                    val leftRadius = calculateDistanceToRectBorder(leftAngle)
                    val rightRadius = calculateDistanceToRectBorder(rightAngle)
                    val blotCircleRadius = (midRadius - circleRadius) / 3
                    val blotCircleOffset =
                        polarToCart(midAngle, midRadius - blotCircleRadius, origin)

                    blotCircles[i] = blotCircleOffset to blotCircleRadius

                    val leftNearDot = polarToCart(leftAngle, circleRadius, origin)
                    val rightNearDot = polarToCart(rightAngle, circleRadius, origin)
                    val commonNearFocus =
                        polarToCart(midAngle, hypot(circleRadius, circleRadius), origin)

                    val (leftFarDot, rightFarDot) = circleSidePoints(
                        blotCircleOffset,
                        midAngle,
                        blotCircleRadius
                    )

                    val rightFarFocus =
                        polarToCart((midAngle + rightAngle) / 2, rightRadius / 2, origin)
                    val leftFarFocus =
                        polarToCart((leftAngle + midAngle) / 2, leftRadius / 2, origin)

                    blotPaths[i].reset()
                    blotPaths[i].moveTo(leftNearDot.x, leftNearDot.y)
                    blotPaths[i].cubicTo(
                        x1 = commonNearFocus.x,
                        y1 = commonNearFocus.y,
                        x2 = leftFarFocus.x,
                        y2 = leftFarFocus.y,
                        x3 = leftFarDot.x,
                        y3 = leftFarDot.y
                    )
                    blotPaths[i].lineTo(rightFarDot.x, rightFarDot.y)
                    blotPaths[i].cubicTo(
                        x1 = rightFarFocus.x,
                        y1 = rightFarFocus.y,
                        x2 = commonNearFocus.x,
                        y2 = commonNearFocus.y,
                        x3 = rightNearDot.x,
                        y3 = rightNearDot.y
                    )
                    blotPaths[i].close()
                }

                clipPath(clipPath, ClipOp.Intersect) {
                    drawCircle(center = origin, radius = circleRadius, color = color)
                    blotCircles.forEach { (center, radius) ->
                        drawCircle(center = center, radius = radius, color = color)
                    }
                    blotPaths.forEach { path ->
                        drawPath(path, color = color)
                    }
                }
            }
        }
    )
}

private fun circleSidePoints(
    circleCenter: Offset,
    midAngle: Float,
    circleRadius: Float
): Pair<Offset, Offset> {
    val leftAngle = midAngle + 100f
    val rightAngle = midAngle - 100f

    val leftPoint = polarToCart(leftAngle, circleRadius, circleCenter)
    val rightPoint = polarToCart(rightAngle, circleRadius, circleCenter)

    return Pair(leftPoint, rightPoint)
}