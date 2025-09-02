package com.example.fool_tool.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fool_tool.ui.navigation.navigation_bar.BottomNavigationRoute
import com.example.fool_tool.ui.navigation.navigation_bar.CustomNavigationBar

@Composable
fun RootNavigator(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentBottomNavigationRoute =
        BottomNavigationRoute.items.firstOrNull { bottomNavigationRoute ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(bottomNavigationRoute::class)
            } == true
        }

    Scaffold(bottomBar = {
        currentBottomNavigationRoute?.let {
            CustomNavigationBar(
                navController = navController,
                currentRoute = currentBottomNavigationRoute
            )
        }

    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavigationRoute.FlashcardGraphRoute,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            flashcardDestination(
                onNavigateToCreateFlashcard = { navController.navigateToCreateFlashcard() },
            )
            smartnoteDestination(
                onNavigateToFlashcardGraph = { navController.navigateToFlashcardGraph() },
                onNavigateToCreateSmartnote = { navController.navigateToCreateSmartnote() },
                onNavigateToEditSmartnote = { id -> navController.navigateToEditSmartnote(id) },
                onNavigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}