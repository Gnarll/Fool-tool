package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fool_tool.ui.screens.smartnote.CreateSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.EditSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.SmartnoteScreen
import kotlinx.serialization.Serializable


@Serializable
object SmartnoteRoute

@Serializable
object CreateSmartnoteRoute

@Serializable
object EditSmartnoteRoute

fun NavController.navigateToFlashcardGraph() {
    popBackStack(route = BottomNavRoute.SmartnoteGraphRoute, inclusive = true)
    navigate(BottomNavRoute.FlashcardGraphRoute)
}

fun NavController.navigateToCreateSmartnote() {
    navigate(CreateSmartnoteRoute)
}

fun NavController.navigateToEditSmartnote() {
    navigate(EditSmartnoteRoute)
}

fun NavGraphBuilder.smartnoteDestination(
    onNavigateToFlashcardGraph: () -> Unit,
    onNavigateToCreateSmartnote: () -> Unit,
    onNavigateToEditSmartnote: (smartnoteId: String) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<BottomNavRoute.SmartnoteGraphRoute>(startDestination = SmartnoteRoute) {
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

        composable<EditSmartnoteRoute> {
            EditSmartnoteScreen(
                onSmartnoteEdited = onNavigateBack,
                modifier = modifier
            )
        }
    }
}