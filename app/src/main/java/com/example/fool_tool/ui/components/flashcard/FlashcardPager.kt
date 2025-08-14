package com.example.fool_tool.ui.components.flashcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.example.fool_tool.ui.model.Flashcard
import kotlin.math.absoluteValue

const val rotationAngle = 30f

@Composable
fun FlashcardPager(
    flashcards: List<Flashcard>,
    onDeleteFlashcard: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState(pageCount = { flashcards.size })

    HorizontalPager(
        state = pagerState,
        key = { flashcards[it].id },
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxSize(),
    ) { page ->

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
                onDeleteItem = onDeleteFlashcard,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .aspectRatio(8f / 5f)
                    .graphicsLayer {
                        this.rotationZ = rotation
                        this.translationY = translation
                        this.alpha = alpha
                        this.scaleX = scale
                        this.scaleY = scale
                    })
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

