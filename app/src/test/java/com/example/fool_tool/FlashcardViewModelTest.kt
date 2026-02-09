package com.example.fool_tool

import com.example.fool_tool.ui.presentation.ui_state.UiState
import com.example.fool_tool.ui.presentation.viewmodel.flashcard.FlashcardViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import kotlin.test.assertEquals
import kotlin.test.assertFalse


class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class)
constructor(val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()) :
    TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description?) {
        Dispatchers.setMain(testDispatcher)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description?) {
        Dispatchers.resetMain()
    }
}

class FlashcardViewModelTest {

    lateinit var fakeRepository: FakeFlashcardRepository
    lateinit var viewModel: FlashcardViewModel

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Before
    fun setupDependencies() {
        fakeRepository = FakeFlashcardRepository()
        viewModel = FlashcardViewModel(fakeRepository)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flashcardViewModel_verifyFlashcardsAppeared() = runTest(UnconfinedTestDispatcher()) {
        assertEquals(viewModel.flashcardStateFlow.value, UiState.Loading)

        backgroundScope.launch {
            viewModel.flashcardStateFlow.collect { }
        }

        var uiState = UiState.Success(fakeRepository.currentFlashcards)
        assertEquals(viewModel.flashcardStateFlow.value, uiState)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flashcardViewModel_deleteItem_ItemDeleted() = runTest(UnconfinedTestDispatcher()) {
        backgroundScope.launch {
            viewModel.flashcardStateFlow.collect { }
        }

        assertEquals(
            UiState.Success(fakeRepository.currentFlashcards),
            viewModel.flashcardStateFlow.value
        )

        val firstFlashcard = fakeRepository.currentFlashcards[0]
        viewModel.deleteFlashcard(firstFlashcard)

        assertFalse {
            (viewModel.flashcardStateFlow.value as UiState.Success).value.contains(firstFlashcard)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun flashcardViewModel_deleteItemById_ItemDeleted() = runTest(UnconfinedTestDispatcher()) {
        backgroundScope.launch {
            viewModel.flashcardStateFlow.collect { }
        }

        assertEquals(
            UiState.Success(fakeRepository.currentFlashcards),
            viewModel.flashcardStateFlow.value
        )

        val firstFlashcard = fakeRepository.currentFlashcards[0]
        viewModel.deleteFlashcardById(firstFlashcard.id)

        assertFalse {
            (viewModel.flashcardStateFlow.value as UiState.Success).value.contains(firstFlashcard)
        }

    }

}