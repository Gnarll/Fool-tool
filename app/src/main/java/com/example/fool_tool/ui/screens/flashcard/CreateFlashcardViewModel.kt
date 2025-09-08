package com.example.fool_tool.ui.screens.flashcard

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.fool_tool.R
import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.ui.utils.FlashcardCreating
import com.example.fool_tool.ui.utils.ValidationError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject


enum class FlashcardValidationError(@StringRes override val messageResId: Int) : ValidationError {
    EMPTY_NATIVE_WORD(R.string.empty_input_error),
    EMPTY_FOREIGN_WORD(R.string.empty_input_error)
}

data class CreateFlashcardFormState(
    val nativeWord: String = "",
    val foreignWord: String = "",
    val nativeWordError: FlashcardValidationError? = null,
    val foreignWordError: FlashcardValidationError? = null
)

@HiltViewModel
class CreateFlashcardViewModel @Inject constructor(private val flashcardRepository: FlashcardRepository) :
    ViewModel() {
    private var _createFlashcardFormState = MutableStateFlow(
        CreateFlashcardFormState(
            nativeWordError = FlashcardValidationError.EMPTY_NATIVE_WORD,
            foreignWordError = FlashcardValidationError.EMPTY_FOREIGN_WORD
        )
    )
    val createFlashcardFormState: StateFlow<CreateFlashcardFormState> =
        _createFlashcardFormState.asStateFlow()

    fun onNativeWordChange(nativeWord: String) {
        _createFlashcardFormState.update {
            it.copy(
                nativeWord = nativeWord,
                nativeWordError = validateNativeWord(nativeWord)
            )
        }
    }

    fun onForeignWordChange(foreignWord: String) {
        _createFlashcardFormState.update {
            it.copy(
                foreignWord = foreignWord,
                foreignWordError = validateForeignWord(foreignWord)
            )
        }
    }

    suspend fun createFlashcard() {
        val flashcard = FlashcardCreating.createFlashcard(
            nativeWord = _createFlashcardFormState.value.nativeWord,
            foreignWord = _createFlashcardFormState.value.foreignWord
        )
        flashcardRepository.insertFlashcard(flashcard)
    }

    private fun validateNativeWord(nativeWord: String): FlashcardValidationError? = when {
        nativeWord.isEmpty() -> FlashcardValidationError.EMPTY_NATIVE_WORD
        else -> null
    }

    private fun validateForeignWord(nativeWord: String): FlashcardValidationError? = when {
        nativeWord.isEmpty() -> FlashcardValidationError.EMPTY_FOREIGN_WORD
        else -> null
    }
}