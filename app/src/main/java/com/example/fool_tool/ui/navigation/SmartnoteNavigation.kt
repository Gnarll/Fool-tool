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

fun NavController.navigateToCreateSmartnote() {
    navigate(Route.CreateSmartnoteRoute)
}

fun NavController.navigateToEditSmartnote(id: String) {
    navigate(Route.EditSmartnoteRoute(id))
}

fun NavGraphBuilder.smartnoteDestination(
    navigateToCreateSmartnote: () -> Unit,
    navigateToEditSmartnote: (smartnoteId: String) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Route.BottomNavigationRoute.SmartnoteGraphRoute>(
        startDestination = Route.BottomNavigationRoute.SmartnoteGraphRoute.startDestination
    ) {
        composable<Route.SmartnoteRoute> {
            SmartnoteScreen(
                onCreateSmartnote = navigateToCreateSmartnote,
                onEditSmartnote = navigateToEditSmartnote,
                modifier = modifier
            )
        }

        composable<Route.CreateSmartnoteRoute> {
            CreateSmartnoteScreen(
                onSmartnoteCreated = navigateBack,
                modifier = modifier
            )
        }

        composable<Route.EditSmartnoteRoute> { backStack ->
            val smartnoteId = backStack.toRoute<Route.EditSmartnoteRoute>().id

            EditSmartnoteScreen(
                onSmartnoteEdited = navigateBack,
                smartnote = smartnoteId,
                modifier = modifier
            )
        }
    }
}