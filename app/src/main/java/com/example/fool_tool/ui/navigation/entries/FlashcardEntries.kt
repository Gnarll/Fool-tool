package com.example.fool_tool.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.fool_tool.ui.navigation.BackStack
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.navigation.pop
import com.example.fool_tool.ui.screens.flashcard.CreateFlashcardScreen
import com.example.fool_tool.ui.screens.flashcard.FlashcardScreen

fun EntryProviderScope<NavKey>.flashcardEntries(backstack: BackStack) {
    entry(key = Route.BottomNavigationRoute.FlashcardRootRoute) {
        FlashcardScreen(
            onCreateFlashcard = { backstack.add(Route.CreateFlashcardRoute) }
        )
    }
    entry(key = Route.CreateFlashcardRoute) {
        CreateFlashcardScreen(
            onFlashcardCreated = { backstack.pop() },
        )
    }
}