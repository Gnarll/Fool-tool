package com.example.fool_tool

import com.example.fool_tool.ui.UiState
import com.example.fool_tool.ui.screens.flashcard.FlashcardViewModel
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
    fun flashcardViewModel_verifyFlashcardFlow() = runTest {
        assertEquals(viewModel.flashcardStateFlow.value, UiState.Loading)

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.flashcardStateFlow.collect { }
        }


        var uiState = UiState.Success(fakeRepository.currentFlashcards)
        assertEquals(viewModel.flashcardStateFlow.value, uiState)

        fakeRepository.deleteFlashcard(fakeRepository.currentFlashcards.first())
        uiState = UiState.Success(fakeRepository.currentFlashcards)
        assertEquals(viewModel.flashcardStateFlow.value, uiState)

        fakeRepository.updateFlashcard(
            fakeRepository.currentFlashcards.first().copy(foreignWord = "new")
        )
        uiState = UiState.Success(fakeRepository.currentFlashcards)
        assertEquals(viewModel.flashcardStateFlow.value, uiState)

        fakeRepository.insertFlashcard(
            fakeRepository.currentFlashcards.last().copy(foreignWord = "OneMoreNew")
        )
        uiState = UiState.Success(fakeRepository.currentFlashcards)
        assertEquals(viewModel.flashcardStateFlow.value, uiState)
    }
}