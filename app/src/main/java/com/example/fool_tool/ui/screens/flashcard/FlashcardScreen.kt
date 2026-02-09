package com.example.fool_tool.ui.screens.flashcard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fool_tool.R
import com.example.fool_tool.domain.model.Flashcard
import com.example.fool_tool.ui.components.flashcard.FlashcardPager
import com.example.fool_tool.ui.components.shared.DeleteItemAlertDialog
import com.example.fool_tool.ui.presentation.ui_state.UiState
import com.example.fool_tool.ui.presentation.ui_state.flashcard.FlashcardDeletionUiState
import com.example.fool_tool.ui.presentation.viewmodel.flashcard.FlashcardViewModel

@Composable
fun FlashcardScreen(
    onCreateFlashcard: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FlashcardViewModel = hiltViewModel(),
) {
    val flashcardState by viewModel.flashcardStateFlow.collectAsStateWithLifecycle()
    val flashcardDeletionState = viewModel.flashcardDeletionUiState.collectAsState().value

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
                    onRequestToDeleteFlashcard = { id, index ->
                        viewModel.setFlashcardDeletionState(
                            FlashcardDeletionUiState.Pending(id = id, index = index)
                        )
                    },
                    onDeleteFlashcard = { id ->
                        viewModel.setFlashcardDeletionState(
                            FlashcardDeletionUiState.NoSelection
                        )
                        viewModel.deleteFlashcardById(id)
                    },
                    flashcardDeletionUiState = flashcardDeletionState,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                if (flashcardDeletionState is FlashcardDeletionUiState.Pending) {
                    DeleteItemAlertDialog(
                        onDismiss = {
                            viewModel.setFlashcardDeletionState(FlashcardDeletionUiState.NoSelection)
                        },
                        onConfirm = {
                            viewModel.setFlashcardDeletionState(
                                FlashcardDeletionUiState.Ready(
                                    id = flashcardDeletionState.id,
                                    index = flashcardDeletionState.index
                                )
                            )
                        },
                    )
                }


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
