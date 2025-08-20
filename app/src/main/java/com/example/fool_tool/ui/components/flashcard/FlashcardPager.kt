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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.example.fool_tool.ui.model.Flashcard
import com.example.fool_tool.ui.screens.flashcard.FlashcardDeletionState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

private const val rotationAngle = 30f

@Composable
fun FlashcardPager(
    flashcards: List<Flashcard>,
    onRequestToDeleteFlashcard: (id: Long, index: Int) -> Unit,
    flashcardDeletionState: FlashcardDeletionState,
    onDeleteFlashcard: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val pagerState = rememberPagerState(pageCount = { flashcards.size })

    var pagerHeight by remember { mutableFloatStateOf(0f) }
    var flashcardHeight by remember { mutableFloatStateOf(0f) }

    val translationYOnDeleting = remember { Animatable(0f) }
    val alphaOnDeleting = remember { Animatable(1f) }

    val flashcardToDeleteIndex = (flashcardDeletionState as? FlashcardDeletionState.Ready)?.index

    LaunchedEffect(flashcardDeletionState) {

        if (flashcardDeletionState !is FlashcardDeletionState.Ready) {
            return@LaunchedEffect
        }

        val animationDuration = 500
        val distanceToBottom = (pagerHeight - flashcardHeight) / 2 + flashcardHeight

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

        val targetPage = when {
            flashcards.size == 1 -> 0
            flashcardDeletionState.index < flashcards.lastIndex -> flashcardDeletionState.index + 1
            else -> flashcardDeletionState.index
        }

        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }

        onDeleteFlashcard(flashcardDeletionState.id)

        translationYOnDeleting.snapTo(0f)
        alphaOnDeleting.snapTo(1f)
    }

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

        val flashcard = flashcards[page]

        val offset =
            (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction.coerceIn(-1f, 1f)
        val positiveOffset = offset.absoluteValue

        val rotation = offset * rotationAngle
        val translation = lerp(start = 0f, stop = -200f, fraction = positiveOffset)
        val alpha = lerp(start = 1f, stop = 0.7f, fraction = positiveOffset)
        val scale = lerp(start = 1f, stop = 0.7f, fraction = positiveOffset)

        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            FlashcardItem(
                flashcard = flashcard,
                onDeleteItem = { id ->
                    onRequestToDeleteFlashcard(id, page)
                },
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(8f / 5f)
                    .onGloballyPositioned { coords ->
                        flashcardHeight = coords.size.height.toFloat()
                    }
                    .graphicsLayer {
                        this.rotationZ = rotation
                        this.translationY =
                            translation + if (flashcardToDeleteIndex == page) translationYOnDeleting.value else 0f
                        this.alpha =
                            alpha * if (flashcardToDeleteIndex == page) alphaOnDeleting.value else 1f
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
    FlashcardPager(
        flashcards = previewFlashcards,
        onDeleteFlashcard = {},
        onRequestToDeleteFlashcard = { _, _ -> },
        flashcardDeletionState = FlashcardDeletionState.NoSelection,
        modifier = modifier
    )
}

private val previewFlashcards = listOf(
    Flashcard(1, "Foreign1", "Native1"),
    Flashcard(2, "Foreign2", "Native2"),
    Flashcard(3, "Foreign3", "Native3"),
    Flashcard(4, "Foreign4", "Native4")
)

