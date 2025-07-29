package com.example.fool_tool.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun RootNavigator(navController: NavHostController) {
    NavHost(navController = navController, startDestination = BottomNavRoute.FlashcardGraphRoute) {
        flashcardDestination(
            onNavigateToSmartnote = { navController.navigateToSmartnoteGraph() },
            onNavigateToCreateFlashcard = { navController.navigateToCreateFlashcard() },
        )
        smartnoteDestination(
            onNavigateToFlashcardGraph = { navController.navigateToFlashcardGraph() },
            onNavigateToCreateSmartnote = { navController.navigateToCreateSmartnote() },
            onNavigateToEditSmartnote = { navController.navigateToEditSmartnote() },
            onNavigateBack = {
                navController.popBackStack()
            }
        )
    }
}