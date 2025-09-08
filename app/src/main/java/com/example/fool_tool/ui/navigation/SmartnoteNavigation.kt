package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.fool_tool.ui.screens.smartnote.CreateSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.EditSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.SmartnoteScreen


fun NavController.navigateToFlashcardGraph() {
    popBackStack(route = Route.BottomNavigationRoute.SmartnoteGraphRoute, inclusive = true)
    navigate(Route.BottomNavigationRoute.FlashcardGraphRoute)
}

fun NavController.navigateToCreateSmartnote() {
    navigate(Route.CreateSmartnoteRoute)
}

fun NavController.navigateToEditSmartnote(id: String) {
    navigate(Route.EditSmartnoteRoute(id))
}

fun NavGraphBuilder.smartnoteDestination(
    onNavigateToFlashcardGraph: () -> Unit,
    onNavigateToCreateSmartnote: () -> Unit,
    onNavigateToEditSmartnote: (smartnoteId: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Route.BottomNavigationRoute.SmartnoteGraphRoute>(startDestination = Route.SmartnoteRoute) {
        composable<Route.SmartnoteRoute> {
            SmartnoteScreen(
                onNavigateToFlashcard = onNavigateToFlashcardGraph,
                onCreateSmartnote = onNavigateToCreateSmartnote,
                onEditSmartnote = onNavigateToEditSmartnote,
                modifier = modifier
            )
        }

        composable<Route.CreateSmartnoteRoute> {
            CreateSmartnoteScreen(
                onSmartnoteCreated = onNavigateBack,
                modifier = modifier
            )
        }

        composable<Route.EditSmartnoteRoute> { backStack ->
            val smartnoteId = backStack.toRoute<Route.EditSmartnoteRoute>().id

            EditSmartnoteScreen(
                onSmartnoteEdited = onNavigateBack,
                smartnote = smartnoteId,
                modifier = modifier
            )
        }
    }
}