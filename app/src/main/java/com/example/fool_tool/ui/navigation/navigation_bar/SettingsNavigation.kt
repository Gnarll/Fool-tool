package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.screens.settings.SettingsScreen


fun NavGraphBuilder.settingsDestination(modifier: Modifier = Modifier) {
    composable<Route.BottomNavigationRoute.SettingsRoute> {
        SettingsScreen(modifier = modifier)
    }
}