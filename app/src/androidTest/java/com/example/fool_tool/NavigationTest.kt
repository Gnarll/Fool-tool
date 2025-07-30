package com.example.fool_tool

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.fool_tool.ui.navigation.BottomNavigationRoute
import com.example.fool_tool.ui.navigation.CreateSmartnoteRoute
import com.example.fool_tool.ui.navigation.RootNavigator
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class NavigationTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    lateinit var navController: TestNavHostController
    lateinit var context: Context

    @Before
    fun setupEnvironment() {
        composeTestRule.setContent {
            context = LocalContext.current
            navController = TestNavHostController(context)
            navController.navigatorProvider.addNavigator(ComposeNavigator())
            navController.navigatorProvider.addNavigator(DialogNavigator())

            RootNavigator(navController = navController)
        }
    }

    @Test
    fun RootNavigator_verifyStartDestinationIsFlashcard() {
        composeTestRule.onNodeWithText(getStringRes(R.string.flashcard_screen_title))
            .assertIsSelected()
    }

    @Test
    fun RootNavigator_navigateToSmartnoteGraph_navigationPerformed() {
        composeTestRule.onNodeWithText(getStringRes(R.string.smartnote_screen_title))
            .performClick()
            .assertIsSelected()

        composeTestRule.onNodeWithText(getStringRes(R.string.flashcard_screen_title))
            .assertIsNotSelected()
    }

    @Test
    fun RootNavigator_navigateFromOneTabToAnother_backStackIsCleared() {
        val initialRoutes = navController.backStack.routes()

        composeTestRule.runOnUiThread {
            navController.navigate(BottomNavigationRoute.SmartnoteGraphRoute)
            navController.navigate(CreateSmartnoteRoute)

        }

        composeTestRule.onNodeWithText(getStringRes(R.string.flashcard_screen_title))
            .performClick()

        val finalRoutes = navController.backStack.routes()

        assertEquals(initialRoutes, finalRoutes)

    }

    @Test
    fun RootNavigator_multiclickOneTab_backStackIsNotFillingUp() {
        composeTestRule.onNodeWithText(getStringRes(R.string.flashcard_screen_title))
            .performClick()

        val initialRoutes = navController.backStack.routes()

        repeat(5) {
            composeTestRule.onNodeWithText(getStringRes(R.string.flashcard_screen_title))
                .performClick()
        }

        val finalRoutes = navController.backStack.routes()

        assertEquals(initialRoutes, finalRoutes)
    }

    private fun List<NavBackStackEntry>.routes() = map { it.destination.route }
    private fun getStringRes(@StringRes string: Int) = context.getString(string)

}