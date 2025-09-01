package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import com.example.fool_tool.ui.navigation.navigation_bar.BottomNavigationRoute
import com.example.fool_tool.ui.screens.smartnote.CreateSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.EditSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.SmartnoteScreen
import kotlinx.serialization.Serializable


@Serializable
object SmartnoteRoute

@Serializable
object CreateSmartnoteRoute

@Serializable
data class EditSmartnoteRoute(val id: String)

fun NavController.navigateToFlashcardGraph() {
    popBackStack(route = BottomNavigationRoute.SmartnoteGraphRoute, inclusive = true)
    navigate(BottomNavigationRoute.FlashcardGraphRoute)
}

fun NavController.navigateToCreateSmartnote() {
    navigate(CreateSmartnoteRoute)
}

fun NavController.navigateToEditSmartnote(id: String) {
    navigate(EditSmartnoteRoute(id))
}

fun NavGraphBuilder.smartnoteDestination(
    onNavigateToFlashcardGraph: () -> Unit,
    onNavigateToCreateSmartnote: () -> Unit,
    onNavigateToEditSmartnote: (smartnoteId: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<BottomNavigationRoute.SmartnoteGraphRoute>(startDestination = SmartnoteRoute) {
        composable<SmartnoteRoute> {
            SmartnoteScreen(
                onNavigateToFlashcard = onNavigateToFlashcardGraph,
                onCreateSmartnote = onNavigateToCreateSmartnote,
                onEditSmartnote = onNavigateToEditSmartnote,
                modifier = modifier
            )
        }

        composable<CreateSmartnoteRoute> {
            CreateSmartnoteScreen(
                onSmartnoteCreated = onNavigateBack,
                modifier = modifier
            )
        }

        composable<EditSmartnoteRoute> { backStack ->
            val smartnoteId = backStack.toRoute<EditSmartnoteRoute>().id

            EditSmartnoteScreen(
                onSmartnoteEdited = onNavigateBack,
                smartnote = smartnoteId,
                modifier = modifier
            )
        }
    }
}