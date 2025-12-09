package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween


class IndicatorAnimationManager private constructor(
    private var currentIndex: Int,
    val shapeAnimation: Animatable<Float, AnimationVector1D>,
    val positionAnimation: Animatable<Float, AnimationVector1D>,
) {
    constructor(isInitiallyStretched: Boolean, initialIndex: Int) : this(
        currentIndex = initialIndex,
        shapeAnimation = Animatable(
            if (isInitiallyStretched) SHAPE_STRETCHED_VALUE
            else SHAPE_SHRANK_VALUE
        ),
        positionAnimation = Animatable(initialIndex.toFloat()),
    )

    private var currentIndicatorState: IndicatorState = IndicatorState.Idle(this)

    suspend fun animateTo(targetIndex: Int) {
        currentIndicatorState.animateTo(targetIndex)
    }

    private sealed class IndicatorState(val indicatorManager: IndicatorAnimationManager) {

        abstract suspend fun animateTo(targetIndex: Int)

        class Idle(indicatorManager: IndicatorAnimationManager) :
            IndicatorState(indicatorManager = indicatorManager) {

            override suspend fun animateTo(targetIndex: Int) {
                if (!indicatorManager.isTargetingToSameIndex(targetIndex)) {
                    indicatorManager.currentIndicatorState = Shrinking(indicatorManager)
                    indicatorManager.animateTo(targetIndex)
                }
            }
        }

        class Shrinking(indicatorManager: IndicatorAnimationManager) :
            IndicatorState(indicatorManager = indicatorManager) {

            override suspend fun animateTo(targetIndex: Int) {
                if (!indicatorManager.isTargetingToSameIndex(targetIndex)) {
                    indicatorManager.shapeAnimation.animateTo(
                        SHAPE_SHRANK_VALUE,
                        shapeMorphAnimationSpec
                    )
                    indicatorManager.currentIndicatorState = Moving(indicatorManager)

                } else {
                    indicatorManager.currentIndicatorState = Stretching(indicatorManager)
                }

                indicatorManager.animateTo(targetIndex)
            }
        }

        class Moving(indicatorManager: IndicatorAnimationManager) :
            IndicatorState(indicatorManager = indicatorManager) {

            override suspend fun animateTo(targetIndex: Int) {
                indicatorManager.positionAnimation.animateTo(
                    targetValue = targetIndex.toFloat(),
                    animationSpec = positionAnimationSpec,
                )

                indicatorManager.currentIndex = targetIndex

                indicatorManager.currentIndicatorState =
                    Stretching(indicatorManager)

                indicatorManager.animateTo(targetIndex)
            }
        }

        class Stretching(indicatorManager: IndicatorAnimationManager) :
            IndicatorState(indicatorManager = indicatorManager) {

            override suspend fun animateTo(targetIndex: Int) {
                if (indicatorManager.isTargetingToSameIndex(targetIndex)) {
                    indicatorManager.shapeAnimation.animateTo(
                        SHAPE_STRETCHED_VALUE,
                        shapeMorphAnimationSpec
                    )
                    indicatorManager.currentIndicatorState = Idle(indicatorManager)

                } else {
                    indicatorManager.currentIndicatorState = Shrinking(indicatorManager)
                }

                indicatorManager.animateTo(targetIndex)
            }
        }
    }


    private fun isTargetingToSameIndex(targetIndex: Int): Boolean = targetIndex == currentIndex

    private companion object {
        val shapeMorphAnimationSpec: AnimationSpec<Float> = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        )
        val positionAnimationSpec: AnimationSpec<Float> =
            tween(durationMillis = 300, easing = EaseInOut)

        const val SHAPE_STRETCHED_VALUE = 1f
        const val SHAPE_SHRANK_VALUE = 0f
    }
}