package com.example.fool_tool.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.fool_tool.ui.screens.reminder.CreateReminderScreen
import com.example.fool_tool.ui.screens.reminder.ReminderScreen

fun NavController.navigateToCreateReminder() {
    navigate(Route.CreateReminderRoute)
}

fun NavGraphBuilder.reminderDestination(
    navigateToCreateReminder: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    navigation<Route.BottomNavigationRoute.ReminderGraphRoute>(
        startDestination =
            Route.BottomNavigationRoute.ReminderGraphRoute.startDestination
    ) {
        composable<Route.ReminderRoute> {
            ReminderScreen(onCreateReminder = navigateToCreateReminder, modifier = modifier)
        }
        composable<Route.CreateReminderRoute> {
            CreateReminderScreen(onReminderCreated = navigateBack)
        }
    }
}