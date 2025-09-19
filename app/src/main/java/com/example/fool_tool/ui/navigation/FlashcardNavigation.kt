package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.fool_tool.ui.screens.flashcard.CreateFlashcardScreen
import com.example.fool_tool.ui.screens.flashcard.FlashcardScreen


fun NavController.navigateToCreateFlashcard() {
    navigate(Route.CreateFlashcardRoute)
}

fun NavGraphBuilder.flashcardDestination(
    navigateToCreateFlashcard: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Route.BottomNavigationRoute.FlashcardGraphRoute>(
        startDestination = Route.BottomNavigationRoute.FlashcardGraphRoute.startDestination
    ) {
        composable<Route.FlashcardRoute> { backStack ->
            FlashcardScreen(
                onCreateFlashcard = navigateToCreateFlashcard,
                modifier = modifier
            )
        }
        composable<Route.CreateFlashcardRoute> { backStack ->
            CreateFlashcardScreen(
                onFlashcardCreated = navigateBack,
                modifier = modifier
            )
        }
    }
}