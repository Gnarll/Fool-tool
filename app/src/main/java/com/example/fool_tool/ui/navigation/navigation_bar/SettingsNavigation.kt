package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.screens.settings.SettingsScreen


fun NavGraphBuilder.settingsDestination(modifier: Modifier = Modifier) {
    navigation<Route.BottomNavigationRoute.SettingsGraphRoute>(startDestination = Route.BottomNavigationRoute.SettingsGraphRoute.startDestination) {
        composable<Route.SettingsRoute> {
            SettingsScreen(modifier = modifier)
        }
    }
}