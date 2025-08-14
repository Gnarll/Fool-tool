package com.example.fool_tool.ui.screens.flashcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.R
import com.example.fool_tool.ui.UiState
import com.example.fool_tool.ui.components.flashcard.FlashcardPager
import com.example.fool_tool.ui.model.Flashcard

@Composable
fun FlashcardScreen(
    onNavigateToSmartnote: () -> Unit,
    onCreateFlashcard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlashcardViewModel = hiltViewModel(),
) {
    val flashcardState by viewModel.flashcardStateFlow.collectAsStateWithLifecycle()

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (flashcardState) {
            is UiState.Loading -> {
                CircularProgressIndicator()
            }

            is UiState.Failure -> {
                Text(
                    text = stringResource(R.string.error_msg_something_went_wrong),
                    color = MaterialTheme.colorScheme.error
                )
            }

            is UiState.Success -> {
                FlashcardPager(
                    flashcards = (flashcardState as UiState.Success<List<Flashcard>>).value,
                    onDeleteFlashcard = { flashcard -> viewModel.deleteFlashcard(flashcard) },
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                FloatingActionButton(
                    onClick = { onCreateFlashcard() },
                    content = {
                        Icon(
                            painter = painterResource(R.drawable.ic_plus),
                            contentDescription = stringResource(R.string.add_flashcard)
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(dimensionResource(R.dimen.padding_x_x_large))
                )
            }
        }
    }
}
