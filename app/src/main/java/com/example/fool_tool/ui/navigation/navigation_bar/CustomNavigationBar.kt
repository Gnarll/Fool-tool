package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import com.example.fool_tool.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


private data class NavigationItem(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: BottomNavigationRoute
)

private val navigationItems: List<NavigationItem> = listOf(
    NavigationItem(
        title = R.string.flashcard_screen_title,
        icon = R.drawable.ic_quiz,
        route = BottomNavigationRoute.FlashcardGraphRoute
    ),
    NavigationItem(
        title = R.string.smartnote_screen_title,
        icon = R.drawable.ic_notes,
        route = BottomNavigationRoute.SmartnoteGraphRoute
    ),
)


private sealed interface IndicatorState {
    object Idle : IndicatorState
    object Shrinking : IndicatorState
    object Moving : IndicatorState
    object Stretching : IndicatorState
}

private fun calculateRelativeOffsetFromIndicator(
    indicatorCoords: Offset,
    navItemsCoords: SnapshotStateMap<Int, Offset>,
    currentRouteIndex: Int
): Offset {
    var resultX = 0f
    var resultY = 0f

    navItemsCoords[currentRouteIndex]?.let {
        resultX = it.x - indicatorCoords.x
        resultY = it.y - indicatorCoords.y
    }

    return Offset(x = resultX, y = resultY)
}

@Composable
fun CustomNavigationBar(navController: NavHostController, currentRoute: BottomNavigationRoute) {
    val coroutineScope = rememberCoroutineScope()
    var currentAnimationJob: Job? by remember { mutableStateOf(null) }
    var currentIndicatorState: IndicatorState by remember { mutableStateOf(IndicatorState.Idle) }

    val navItemsCoordinates = remember { mutableStateMapOf<Int, Offset>() }
    var indicatorCoordinates: Offset by remember { mutableStateOf(Offset.Zero) }

    val relativeIndicatorOffsetAnimatable = remember { Animatable(0f) }
    val indicatorShapeProgressAnimatable = remember { Animatable(1f) }

    CustomNavigationBarLayout(
        innerPadding = PaddingValues(
            top = dimensionResource(R.dimen.padding_medium),
            start = dimensionResource(R.dimen.padding_x_small),
            end = dimensionResource(R.dimen.padding_medium),
        ),
        floatingNavigationIndicator = {
            FloatingNavigationIndicator(
                shapeProgress = indicatorShapeProgressAnimatable.value,
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        indicatorCoordinates = coordinates.positionInParent()
                    }
                    .graphicsLayer {
                        this.translationX = relativeIndicatorOffsetAnimatable.value
                    }

            )
        },
        navigationBarBackground = { NavigationBarBackground() },
    ) {
        navigationItems.forEachIndexed { navItemIndex, navItem ->
            val isSelected = navItem.route == currentRoute

            CustomNavigationBarItem(
                onClick = {
                    if (isSelected) return@CustomNavigationBarItem

                    val targetPositionX = calculateRelativeOffsetFromIndicator(
                        indicatorCoordinates,
                        navItemsCoordinates,
                        navItemIndex
                    ).x
                    currentAnimationJob?.cancel()

                    currentAnimationJob = coroutineScope.launch {
                        when (currentIndicatorState) {
                            is IndicatorState.Idle -> {
                                currentIndicatorState = IndicatorState.Shrinking
                                indicatorShapeProgressAnimatable.animateTo(0f)
                                currentIndicatorState = IndicatorState.Moving
                                relativeIndicatorOffsetAnimatable.animateTo(targetPositionX)
                                currentIndicatorState = IndicatorState.Stretching
                                indicatorShapeProgressAnimatable.animateTo(1f)
                                currentIndicatorState = IndicatorState.Idle
                            }

                            is IndicatorState.Shrinking -> {
                                indicatorShapeProgressAnimatable.animateTo(0f)
                                currentIndicatorState = IndicatorState.Moving
                                relativeIndicatorOffsetAnimatable.animateTo(targetPositionX)
                                currentIndicatorState = IndicatorState.Stretching
                                indicatorShapeProgressAnimatable.animateTo(1f)
                                currentIndicatorState = IndicatorState.Idle
                            }

                            is IndicatorState.Moving -> {
                                relativeIndicatorOffsetAnimatable.animateTo(targetPositionX)
                                currentIndicatorState = IndicatorState.Stretching
                                indicatorShapeProgressAnimatable.animateTo(1f)
                                currentIndicatorState = IndicatorState.Idle
                            }

                            is IndicatorState.Stretching -> {
                                currentIndicatorState = IndicatorState.Shrinking
                                indicatorShapeProgressAnimatable.animateTo(0f)
                                currentIndicatorState = IndicatorState.Moving
                                relativeIndicatorOffsetAnimatable.animateTo(targetPositionX)
                                currentIndicatorState = IndicatorState.Stretching
                                indicatorShapeProgressAnimatable.animateTo(1f)
                                currentIndicatorState = IndicatorState.Idle
                            }
                        }

                    }

                    navController.navigate(navItem.route) {
                        popUpTo(currentRoute::class) {
                            saveState = true
                            inclusive = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                isSelected = isSelected,
                icon = painterResource(navItem.icon),
                labelText = stringResource(navItem.title),
                modifier = Modifier.onGloballyPositioned { coordinates ->
                    navItemsCoordinates[navItemIndex] = coordinates.positionInParent()
                }
            )
        }
    }
}


@Composable
fun NavigationBarBackground(modifier: Modifier = Modifier) {
    Spacer(modifier = modifier.background(color = Color.Blue))
}


@Composable
fun CustomNavigationBarItem(
    onClick: () -> Unit,
    isSelected: Boolean,
    icon: Painter,
    labelText: String,
    modifier: Modifier = Modifier,
) {

    val itemColor = if (isSelected) Color.Black else Color.Black.copy(alpha = 0.5f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_x_small)
        ),
        modifier = modifier
            .clickable(
                onClick = onClick, role = Role.Tab,
                indication = null,
                interactionSource = null,
            )
    ) {
        Icon(
            painter = icon,
            contentDescription = labelText,
            tint = itemColor,
            modifier = Modifier.size(dimensionResource(R.dimen.nav_icon_size))
        )
        Text(
            text = labelText,
            color = itemColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}