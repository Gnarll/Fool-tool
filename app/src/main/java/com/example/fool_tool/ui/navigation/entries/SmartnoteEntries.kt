package com.example.fool_tool.ui.navigation.entries

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import com.example.fool_tool.ui.navigation.BackStack
import com.example.fool_tool.ui.navigation.Route
import com.example.fool_tool.ui.navigation.pop
import com.example.fool_tool.ui.screens.smartnote.CreateSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.EditSmartnoteScreen
import com.example.fool_tool.ui.screens.smartnote.SmartnoteScreen


fun EntryProviderScope<NavKey>.smartnoteEntries(backstack: BackStack) {
    entry(key = Route.BottomNavigationRoute.SmartnoteRootRoute) {
        SmartnoteScreen(
            onCreateSmartnote = {
                backstack.add(Route.CreateSmartnoteRoute)
            },
            onEditSmartnote = { id ->
                backstack.add(Route.EditSmartnoteRoute(id))
            },
        )
    }
    entry(key = Route.CreateSmartnoteRoute) {
        CreateSmartnoteScreen(onSmartnoteCreated = { backstack.pop() })
    }
    entry<Route.EditSmartnoteRoute> { key ->
        EditSmartnoteScreen(onSmartnoteEdited = { backstack.pop() }, smartnoteId = key.id)
    }
}