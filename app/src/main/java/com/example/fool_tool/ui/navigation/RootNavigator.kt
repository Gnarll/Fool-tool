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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.fool_tool.R
import com.example.fool_tool.ui.navigation.navigation_bar.CustomNavigationBar
import com.example.fool_tool.ui.navigation.navigation_bar.settingsDestination
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigator(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var savedBottomNavigationRoute by rememberSaveable(stateSaver = bottomNavRouteSaver) {
        mutableStateOf(null)
    }

    var shouldShowBottomNavBar by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(currentDestination) {
        Route.bottomNavRoutes.firstOrNull { graphRoute ->
            currentDestination?.hierarchy?.any {
                it.hasRoute(graphRoute::class)
            } == true
        }?.let {
            savedBottomNavigationRoute = it
        }
        currentDestination?.let {
            shouldShowBottomNavBar = Route.routesShouldShowBottomNavigation.any { route ->
                currentDestination?.hasRoute(route::class) == true
            }

        }
    }

    Scaffold(
        bottomBar = {
            if (savedBottomNavigationRoute != null) {
                AnimatedVisibility(visible = shouldShowBottomNavBar) {
                    CustomNavigationBar(
                        navController = navController,
                        currentRoute = savedBottomNavigationRoute as Route.BottomNavigationRoute
                    )
                }
            }

        },
        topBar = {
            if (savedBottomNavigationRoute != null) {
                AnimatedVisibility(visible = !shouldShowBottomNavBar) {
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

private val bottomNavRouteSaver: Saver<Route.BottomNavigationRoute?, String> = Saver(
    save = { route ->
        route?.let { Json.encodeToString(Route.serializer(), it) }
    },
    restore = { json ->
        json?.let {
            Json.decodeFromString(
                Route.serializer(),
                it
            ) as? Route.BottomNavigationRoute
        }
    }
)