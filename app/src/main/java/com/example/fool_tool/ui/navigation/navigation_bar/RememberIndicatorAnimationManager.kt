package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberIndicatorAnimationManager(): IndicatorAnimationManager {

    val shapeAnimation: Animatable<Float, AnimationVector1D> = remember {
        Animatable(
            0f
        )
    }
    val offsetAnimation: Animatable<Float, AnimationVector1D> = remember { Animatable(0f) }

    return remember {
        IndicatorAnimationManager(
            shapeAnimation = shapeAnimation,
            offsetAnimation = offsetAnimation,
        )
    }
}