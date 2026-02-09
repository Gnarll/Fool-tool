package com.example.fool_tool.ui.screens.flashcard

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.ValidatedTextField
import com.example.fool_tool.ui.presentation.viewmodel.flashcard.CreateFlashcardViewModel
import kotlinx.coroutines.launch


@Composable
fun CreateFlashcardScreen(
    onFlashcardCreated: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateFlashcardViewModel = hiltViewModel()
) {
    val formState = viewModel.createFlashcardFormUiState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val createdToastText = stringResource(R.string.flashcard_is_created)

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
            horizontalAlignment = Alignment.Start
        ) {
            ValidatedTextField(
                value = formState.nativeWord,
                onValueChange = viewModel::onNativeWordChange,
                labelText =
                    stringResource(R.string.native_word),
                error = formState.nativeWordError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            ValidatedTextField(
                value = formState.foreignWord,
                onValueChange = viewModel::onForeignWordChange,
                labelText =
                    stringResource(R.string.foreign_word),
                error = formState.foreignWordError,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )

            Button(
                onClick = {
                    coroutineScope.launch {
                        viewModel.createFlashcard()
                        Toast.makeText(
                            context,
                            createdToastText,
                            Toast.LENGTH_SHORT
                        ).show()
                        onFlashcardCreated()
                    }
                },
                enabled = formState.nativeWordError == null && formState.foreignWordError == null
            ) {
                Text(text = stringResource(R.string.create_flashcard))
            }
        }
    }
}