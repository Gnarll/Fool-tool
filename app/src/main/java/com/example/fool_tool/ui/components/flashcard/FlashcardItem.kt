package com.example.fool_tool.ui.components.flashcard

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fool_tool.R
import com.example.fool_tool.ui.model.Flashcard

@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onDeleteItem: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var isForeignSideVisible by rememberSaveable { mutableStateOf(true) }

    val rotation by animateFloatAsState(
        targetValue = if (isForeignSideVisible) 0f else 180f,
        animationSpec = tween(durationMillis = 600),
        label = "flipAnimation"
    )

    Card(
        modifier = modifier.graphicsLayer {
            rotationY = rotation
            cameraDistance = 8 * density
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            val isFront = rotation < 90f
            val text = if (isFront) flashcard.foreignWord else flashcard.nativeWord

            Text(
                text = text,
                modifier = Modifier
                    .padding(8.dp)
                    .graphicsLayer {
                        if (!isFront) rotationY = 180f
                    }
            )

            IconButton(
                onClick = { isForeignSideVisible = !isForeignSideVisible },
                modifier = Modifier.align(if (isFront) Alignment.BottomStart else Alignment.BottomEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_turn_over),
                    contentDescription = stringResource(R.string.turn_over)
                )
            }

            IconButton(
                onClick = { onDeleteItem(flashcard.id) },
                modifier = Modifier.align(if (isFront) Alignment.TopEnd else Alignment.TopStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_trash_can),
                    contentDescription = stringResource(R.string.delete_word)
                )
            }
        }
    }
}

@Composable
@Preview
fun FlashcardItemPreview() {
    FlashcardItem(
        flashcard = Flashcard(id = 1, foreignWord = "Foreign", nativeWord = "Native"),
        onDeleteItem = {},
        modifier = Modifier
            .width(200.dp)
            .height(120.dp)
    )
}