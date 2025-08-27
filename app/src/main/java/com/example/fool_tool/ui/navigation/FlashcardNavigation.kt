package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import com.example.fool_tool.ui.navigation.navigation_bar.BottomNavigationRoute
import com.example.fool_tool.ui.screens.flashcard.CreateFlashcardDialog
import com.example.fool_tool.ui.screens.flashcard.FlashcardScreen
import kotlinx.serialization.Serializable


@Serializable
object FlashcardRoute

@Serializable
object CreateFlashcardRoute

fun NavController.navigateToSmartnoteGraph() {
    navigate(route = BottomNavigationRoute.SmartnoteGraphRoute) {
        popUpTo<BottomNavigationRoute.FlashcardGraphRoute> { inclusive = true }
    }
}

fun NavController.navigateToCreateFlashcard() {
    navigate(CreateFlashcardRoute)
}

fun NavGraphBuilder.flashcardDestination(
    onNavigateToSmartnote: () -> Unit,
    onNavigateToCreateFlashcard: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<BottomNavigationRoute.FlashcardGraphRoute>(
        startDestination = FlashcardRoute
    ) {
        composable<FlashcardRoute> { backStack ->
            FlashcardScreen(
                onNavigateToSmartnote = onNavigateToSmartnote,
                onCreateFlashcard = onNavigateToCreateFlashcard,
                modifier = modifier
            )
        }
        dialog<CreateFlashcardRoute> { backStack ->
            CreateFlashcardDialog(modifier = modifier)
        }
    }
}