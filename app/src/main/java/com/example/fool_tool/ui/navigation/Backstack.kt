package com.example.fool_tool.ui.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

typealias BackStack = NavBackStack<NavKey>

fun BackStack.pop() = removeLastOrNull()

fun BackStack.shouldShowTopBar(): Boolean {
    val lastTwoRoutes = takeLast(2)
    return lastTwoRoutes.size == 2
            && lastTwoRoutes[0] is Route.BottomNavigationRoute
            && lastTwoRoutes[1] !is Route.BottomNavigationRoute
}

fun BackStack.shouldShowBottomBar() = all { it is Route.BottomNavigationRoute }

fun BackStack.currentBottomNavRoute(): Route.BottomNavigationRoute =
    last { it is Route.BottomNavigationRoute } as Route.BottomNavigationRoute
