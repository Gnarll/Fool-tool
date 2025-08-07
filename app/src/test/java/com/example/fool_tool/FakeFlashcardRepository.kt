package com.example.fool_tool

import com.example.fool_tool.data.repositories.FlashcardRepository
import com.example.fool_tool.ui.model.Flashcard
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update


class FakeFlashcardRepository : FlashcardRepository {
    private val _flashcardsFlow = MutableStateFlow(
        listOf(
            Flashcard(1, "foreign1", "native1"),
            Flashcard(2, "foreign2", "native2"),
            Flashcard(3, "foreign3", "native3")
        )
    )

    val currentFlashcards: List<Flashcard>
        get() = _flashcardsFlow.value

    override fun getAllFlashcardsStream(): Flow<List<Flashcard>> = _flashcardsFlow

    override suspend fun getFlashcardById(id: Long): Flashcard = _flashcardsFlow.value.first {
        it.id == id
    }

    override suspend fun insertFlashcard(flashcard: Flashcard) {
        _flashcardsFlow.update { it + flashcard }
    }

    override suspend fun updateFlashcard(flashcard: Flashcard) {
        _flashcardsFlow.update { prevState ->
            prevState.map {
                if (it.id == flashcard.id) flashcard else it
            }
        }
    }

    override suspend fun deleteFlashcard(flashcard: Flashcard) {
        _flashcardsFlow.update { prevState ->
            prevState.filter {
                flashcard.id != it.id
            }
        }
    }
}