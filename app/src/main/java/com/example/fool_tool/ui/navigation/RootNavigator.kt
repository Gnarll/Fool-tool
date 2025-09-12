package com.example.fool_tool.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fool_tool.R
import com.example.fool_tool.ui.navigation.navigation_bar.CustomNavigationBar
import com.example.fool_tool.ui.navigation.navigation_bar.NavigationItem
import com.example.fool_tool.ui.navigation.navigation_bar.settingsDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigator(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val isBottomBarVisible = remember(currentDestination) {
        currentDestination?.let {
            Route.routesShouldShowBottomNavigation.any {
                currentDestination.hasRoute(it::class)
            }
        } ?: true
    }

    val isTopBarVisible = remember(isBottomBarVisible, currentDestination) {
        currentDestination?.let {
            !isBottomBarVisible
        } ?: false
    }

    val navigationItems: List<NavigationItem> = remember {
        listOf(
            NavigationItem(
                title = R.string.flashcard_screen_title,
                icon = R.drawable.ic_quiz,
                route = Route.BottomNavigationRoute.FlashcardGraphRoute,
            ),
            NavigationItem(
                title = R.string.smartnote_screen_title,
                icon = R.drawable.ic_notes,
                route = Route.BottomNavigationRoute.SmartnoteGraphRoute,
            ),
            NavigationItem(
                title = R.string.settings,
                icon = R.drawable.ic_settings,
                route = Route.BottomNavigationRoute.SettingsRoute,
            ),
        )
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(visible = isBottomBarVisible) {
                CustomNavigationBar(
                    navigationItems = navigationItems,
                    navigateTo = { route ->
                        currentDestination?.let {
                            if (!currentDestination.hasRoute(route::class)) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }

                            }
                        }
                    }
                )
            }
        },
        topBar = {
            AnimatedVisibility(visible = isTopBarVisible) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_top_left),
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    })
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
            settingsDestination()
        }
    }
}
