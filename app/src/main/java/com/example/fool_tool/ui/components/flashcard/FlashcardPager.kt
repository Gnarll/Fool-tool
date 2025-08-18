package com.example.fool_tool.ui.components.flashcard

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.example.fool_tool.ui.model.Flashcard
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

const val rotationAngle = 30f

@Composable
fun FlashcardPager(
    flashcards: List<Flashcard>,
    onDeleteFlashcard: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { flashcards.size })

    var pagerHeight by remember { mutableFloatStateOf(0f) }
    var flashcardHeight by remember { mutableFloatStateOf(0f) }

    HorizontalPager(
        state = pagerState,
        key = { flashcards[it].id },
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxSize()
            .onGloballyPositioned { coords ->
                pagerHeight = coords.size.height.toFloat()
            }
    ) { page ->

        var isMarkedToDelete by remember { mutableStateOf(false) }

        val translationYOnDeleting = remember { Animatable(initialValue = 0f) }
        val alphaOnDeleting = remember { Animatable(initialValue = 1f) }

        LaunchedEffect(isMarkedToDelete) {
            if (isMarkedToDelete) {
                val animationDuration = 500
                val distanceToBottom = (pagerHeight - flashcardHeight) / 2 + flashcardHeight

                val jumpBezierEasing = CubicBezierEasing(0.3f, -0.56f, 0.456f, 0.67f)

                val deferredAnimationProgress = async {
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

                deferredAnimationProgress.await()

                val targetPage = when {
                    flashcards.size == 1 -> 0
                    page < flashcards.lastIndex -> page + 1
                    else -> page - 1
                }

                launch {
                    if (targetPage != pagerState.currentPage) {
                        pagerState.scrollToPage(targetPage)
                    }
                }.join()

                onDeleteFlashcard(flashcards[page])
            }
        }


        val markToDeleteItem: (Flashcard) -> Unit = remember {
            {
                isMarkedToDelete = true
            }
        }

        val offset =
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction.coerceIn(
                -1f,
                1f
            ) // [-1;1]
        val positiveOffset = offset.absoluteValue // [0; 1]

        val rotation = offset * rotationAngle
        val translation = lerp(start = 0f, stop = -200f, positiveOffset)
        val alpha = lerp(start = 1f, stop = 0.7f, positiveOffset)
        val scale = lerp(start = 1f, stop = 0.7f, positiveOffset)

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            FlashcardItem(
                flashcard = flashcards[page],
                onDeleteItem = markToDeleteItem,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(8f / 5f)
                    .onGloballyPositioned { coords ->
                        flashcardHeight = coords.size.height.toFloat()
                    }
                    .graphicsLayer {
                        this.rotationZ = rotation
                        this.translationY = translation + translationYOnDeleting.value
                        this.alpha = alpha * alphaOnDeleting.value
                        this.scaleX = scale
                        this.scaleY = scale
                    }
            )
        }

    }

}

@Preview
@Composable
fun FlashcardPagerPreview(modifier: Modifier = Modifier) {
    FlashcardPager(flashcards = previewFlashcards, onDeleteFlashcard = {}, modifier = modifier)
}

private val previewFlashcards = listOf(
    Flashcard(1, "Foreign1", "Native1"),
    Flashcard(2, "Foreign2", "Native2"),
    Flashcard(3, "Foreign3", "Native3"),
    Flashcard(4, "Foreign4", "Native4")
)

