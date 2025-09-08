package com.example.fool_tool.ui.screens.flashcard

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.ValidatedTextField
import kotlinx.coroutines.launch


@Composable
fun CreateFlashcardScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateFlashcardViewModel = hiltViewModel()
) {
    val formState = viewModel.createFlashcardFormState.collectAsState().value
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Log.i("input", "${formState.nativeWordError}  ${formState.foreignWordError}")

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
                error = formState.nativeWordError
            )

            ValidatedTextField(
                value = formState.foreignWord,
                onValueChange = viewModel::onForeignWordChange,
                labelText =
                    stringResource(R.string.foreign_word),
                error = formState.foreignWordError
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
                        onNavigateBack()
                    }
                },
                enabled = formState.nativeWordError == null && formState.foreignWordError == null
            ) {
                Text(text = stringResource(R.string.create_flashcard))
            }
        }
    }
}