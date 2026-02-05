package com.example.fool_tool.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.example.fool_tool.R
import com.example.fool_tool.ui.navigation.entries.flashcardEntries
import com.example.fool_tool.ui.navigation.entries.reminderEntries
import com.example.fool_tool.ui.navigation.entries.smartnoteEntries
import com.example.fool_tool.ui.navigation.navigation_bar.CustomNavigationBar
import com.example.fool_tool.ui.navigation.navigation_bar.NavigationItem
import com.example.fool_tool.ui.screens.settings.SettingsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootNavigator(backStack: BackStack) {
    val navigationItems: List<NavigationItem> = remember {
        listOf(
            NavigationItem(
                title = R.string.flashcard_screen_title,
                icon = R.drawable.ic_quiz,
                route = Route.BottomNavigationRoute.FlashcardRootRoute,
            ),
            NavigationItem(
                title = R.string.smartnote_screen_title,
                icon = R.drawable.ic_notes,
                route = Route.BottomNavigationRoute.SmartnoteRootRoute,
            ),
            NavigationItem(
                title = R.string.reminder_screen_title,
                icon = R.drawable.ic_calendar,
                route = Route.BottomNavigationRoute.ReminderRootRoute(),
            ),
            NavigationItem(
                title = R.string.settings,
                icon = R.drawable.ic_settings,
                route = Route.BottomNavigationRoute.SettingsRootRoute,
            ),
        )
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = backStack.shouldShowBottomBar(),
                enter = slideInVertically { it },
                exit = slideOutVertically { it })
            {
                CustomNavigationBar(
                    navigationItems = navigationItems,
                    currentNavRoute = backStack.currentBottomNavRoute(),
                    navigateTo = { route ->
                        if (backStack.last() != route) {
                            backStack.add(route)
                        }
                    }

                )
            }
        },
        topBar = {
            AnimatedVisibility(
                visible = backStack.shouldShowTopBar(),
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it }
            ) {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(onClick = { backStack.pop() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_arrow_top_left),
                                contentDescription = stringResource(R.string.go_back)
                            )
                        }
                    })
            }
        }
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            backStack = backStack,
            onBack = {
                backStack.pop()
            },
            entryDecorators = listOf(

                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                flashcardEntries(backstack = backStack)
                reminderEntries(backstack = backStack)
                smartnoteEntries(backstack = backStack)
                entry(key = Route.BottomNavigationRoute.SettingsRootRoute) {
                    SettingsScreen()
                }
            }
        )
    }
}
