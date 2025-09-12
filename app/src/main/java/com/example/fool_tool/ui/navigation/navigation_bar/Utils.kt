package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.ui.geometry.Offset

fun calculateRelativeOffsetFromIndicator(
    indicatorCoords: Offset,
    navItemsCoords: Map<Int, Offset>,
    currentRouteIndex: Int
): Offset {
    var resultX = 0f
    var resultY = 0f

    navItemsCoords[currentRouteIndex]?.let {
        resultX = it.x - indicatorCoords.x
        resultY = it.y - indicatorCoords.y
    }

    return Offset(x = resultX, y = resultY)
}


class IndicatorAnimationManager(
    val shapeAnimation: Animatable<Float, AnimationVector1D>,
    val offsetAnimation: Animatable<Float, AnimationVector1D>
) {
    private var currentIndicatorState: IndicatorState = IndicatorState.Idle

    suspend fun animateTo(targetX: Float) {
        when (currentIndicatorState) {
            is IndicatorState.Idle -> {
                fromIdleStateAnimateTo(targetX)
            }

            is IndicatorState.Shrinking -> {
                fromShrinkingStateAnimateTo(targetX)
            }

            is IndicatorState.Moving -> {
                fromMovingStateAnimateTo(targetX)
            }

            is IndicatorState.Stretching -> {
                fromStretchingStateAnimateTo(targetX)
            }
        }
    }

    private suspend fun fromIdleStateAnimateTo(targetX: Float) {
        currentIndicatorState = IndicatorState.Shrinking
        shapeAnimation.animateTo(
            0f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Moving
        offsetAnimation.animateTo(targetValue = targetX, animationSpec = offsetAnimationSpec)
        currentIndicatorState = IndicatorState.Stretching
        shapeAnimation.animateTo(
            1f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Idle
    }

    private suspend fun fromShrinkingStateAnimateTo(targetX: Float) {
        shapeAnimation.animateTo(
            0f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Moving
        offsetAnimation.animateTo(targetValue = targetX, animationSpec = offsetAnimationSpec)
        currentIndicatorState = IndicatorState.Stretching
        shapeAnimation.animateTo(
            1f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Idle
    }

    private suspend fun fromMovingStateAnimateTo(targetX: Float) {
        offsetAnimation.animateTo(
            targetValue = targetX,
            animationSpec = offsetAnimationSpec,
        )
        currentIndicatorState = IndicatorState.Stretching
        shapeAnimation.animateTo(
            1f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Idle
    }

    private suspend fun fromStretchingStateAnimateTo(targetX: Float) {
        currentIndicatorState = IndicatorState.Shrinking
        shapeAnimation.animateTo(
            0f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Moving
        offsetAnimation.animateTo(targetValue = targetX, animationSpec = offsetAnimationSpec)
        currentIndicatorState = IndicatorState.Stretching
        shapeAnimation.animateTo(
            1f,
            shapeMorphAnimationSpec
        )
        currentIndicatorState = IndicatorState.Idle
    }


    private companion object {
        sealed interface IndicatorState {
            object Idle : IndicatorState
            object Shrinking : IndicatorState
            object Moving : IndicatorState
            object Stretching : IndicatorState
        }

        val shapeMorphAnimationSpec: AnimationSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
        val offsetAnimationSpec: AnimationSpec<Float> =
            tween(durationMillis = 300, easing = EaseInOut)
    }
}