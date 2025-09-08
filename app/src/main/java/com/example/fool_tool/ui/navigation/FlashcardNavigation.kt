package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fool_tool.ui.navigation.navigation_bar.BottomNavigationRoute
import com.example.fool_tool.ui.screens.flashcard.CreateFlashcardScreen
import com.example.fool_tool.ui.screens.flashcard.FlashcardScreen
import kotlinx.serialization.Serializable


@Serializable
object FlashcardRoute

@Serializable
object CreateFlashcardRoute

fun NavController.navigateToCreateFlashcard() {
    navigate(CreateFlashcardRoute)
}

fun NavGraphBuilder.flashcardDestination(
    onNavigateToCreateFlashcard: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<BottomNavigationRoute.FlashcardGraphRoute>(
        startDestination = FlashcardRoute
    ) {
        composable<FlashcardRoute> { backStack ->
            FlashcardScreen(
                onCreateFlashcard = onNavigateToCreateFlashcard,
                modifier = modifier
            )
        }
        composable<CreateFlashcardRoute> { backStack ->
            CreateFlashcardScreen(
                onNavigateBack = onNavigateBack,
                modifier = modifier
            )
        }
    }
}