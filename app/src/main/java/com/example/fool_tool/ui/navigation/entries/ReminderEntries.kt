package com.example.fool_tool.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.fool_tool.ui.navigation.BackStack
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.navigation.pop
import com.example.fool_tool.ui.screens.reminder.CreateReminderScreen
import com.example.fool_tool.ui.screens.reminder.ReminderScreen


fun EntryProviderScope<NavKey>.reminderEntries(backstack: BackStack) {
    entry(key = Route.BottomNavigationRoute.ReminderRootRoute) {
        ReminderScreen(
            onCreateReminder = { backstack.add(Route.CreateReminderRoute) }
        )
    }
    entry(key = Route.CreateReminderRoute) {
        CreateReminderScreen(onNavigateBack = { backstack.pop() })
    }
}