package com.example.fool_tool.ui.navigation

import androidx.compose.animation.AnimatedVisibility
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
import com.example.fool_tool.ui.navigation.navigation_bar.CustomNavigationBar

@Composable
fun RootNavigator(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentBottomNavigationRoute =
        Route.graphRoutes.firstOrNull { graphRoute ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(graphRoute::class)
            } == true
        }

    val shouldShowBottomNavBar = Route.routesShouldShowBottomNavigation.any { route ->
        currentDestination?.hasRoute(route::class) == true
    }

    Scaffold(bottomBar = {
        if (currentBottomNavigationRoute != null) {
            AnimatedVisibility(visible = shouldShowBottomNavBar) {
                CustomNavigationBar(
                    navController = navController,
                    currentRoute = currentBottomNavigationRoute
                )
            }
        }

    }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Route.BottomNavigationRoute.FlashcardGraphRoute,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            flashcardDestination(
                onNavigateToCreateFlashcard = { navController.navigateToCreateFlashcard() },
                onNavigateBack = {
                    navController.popBackStack()
                }
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