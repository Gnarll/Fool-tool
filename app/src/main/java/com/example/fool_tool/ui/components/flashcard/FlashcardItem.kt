package com.example.fool_tool.ui.components.flashcard

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fool_tool.R
import com.example.fool_tool.ui.model.Flashcard


@Composable
fun FlashcardItem(
    flashcard: Flashcard,
    onDeleteItem: (Flashcard) -> Unit,
    modifier: Modifier = Modifier
) {

    var isForeignSideVisible by rememberSaveable { mutableStateOf(true) }

    Card(
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = if (isForeignSideVisible) flashcard.foreignWord else flashcard.nativeWord,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small))
            )

            IconButton(
                onClick = { isForeignSideVisible = !isForeignSideVisible },
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_turn_over),
                    contentDescription = stringResource(R.string.turn_over)
                )
            }

            IconButton(
                onClick = { onDeleteItem(flashcard) },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_trash_can),
                    contentDescription = stringResource(R.string.turn_over)
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