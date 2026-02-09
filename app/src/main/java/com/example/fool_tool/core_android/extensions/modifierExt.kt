package com.example.fool_tool.core_android.extensions

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.example.fool_tool.ui.presentation.ui_state.flashcard.FlashcardDeletionUiState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun <T> Modifier.animatePagerItemDeletion(
    flashcardDeletionUiState: FlashcardDeletionUiState,
    index: Int,
    distanceToBottom: Float,
    list: List<T>,
    pagerState: PagerState,
    onStart: () -> Unit,
    onFinish: (idToDelete: Long) -> Unit,
): Modifier {
    val translationYOnDeleting = remember { Animatable(0f) }
    val alphaOnDeleting = remember { Animatable(1f) }

    LaunchedEffect(flashcardDeletionUiState) {
        if (flashcardDeletionUiState is FlashcardDeletionUiState.Ready &&
            flashcardDeletionUiState.index == index
        ) {
            onStart()

            val animationDuration = 500
            val jumpBezierEasing = CubicBezierEasing(0.3f, -0.56f, 0.456f, 0.67f)

            coroutineScope {
                launch {
                    translationYOnDeleting.animateTo(
                        targetValue = distanceToBottom,
                        animationSpec = tween(
                            durationMillis = animationDuration,
                            easing = jumpBezierEasing
                        )
                    )
                }
                launch {
                    alphaOnDeleting.animateTo(
                        targetValue = 0.5f,
                        animationSpec = tween(durationMillis = animationDuration)
                    )
                }
            }

            try {
                if (list.size > 1 && index != list.lastIndex) {
                    pagerState.animateScrollToPage(page = index + 1)
                } else if (index == list.lastIndex) {
                    pagerState.animateScrollToPage(page = index - 1)
                }
            } finally {
                onFinish(flashcardDeletionUiState.id)
            }
        }
    }

    return this.graphicsLayer {
        this.translationY = translationYOnDeleting.value
        this.alpha = alphaOnDeleting.value
    }

}