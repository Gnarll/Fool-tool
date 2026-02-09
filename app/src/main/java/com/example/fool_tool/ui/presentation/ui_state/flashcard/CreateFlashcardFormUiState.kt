package com.example.fool_tool.ui.presentation.ui_state.flashcard

import androidx.compose.runtime.Immutable
import com.example.fool_tool.core_android.util.validation.ValidationError

@Immutable
data class CreateFlashcardFormUiState(
    val nativeWord: String = "",
    val foreignWord: String = "",
    val nativeWordError: ValidationError? = null,
    val foreignWordError: ValidationError? = null
)