package com.example.fool_tool.ui.navigation.navigation_bar

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import com.example.fool_tool.R

@Composable
fun CustomNavigationBarLayout(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(
        top = dimensionResource(R.dimen.padding_small),
    ),
    floatingNavigationIndicator: @Composable () -> Unit,
    navigationBarBackground: @Composable () -> Unit,
    navigationBarItems: @Composable () -> Unit,
) {
    val layoutDirection = LocalLayoutDirection.current


    val (leftPadding, rightPadding, topPadding, bottomPadding) = with(LocalDensity.current) {
        val safeAreaPaddingValues = WindowInsets.navigationBars.asPaddingValues()

        listOf(
            innerPadding.calculateLeftPadding(layoutDirection).roundToPx(),
            innerPadding.calculateRightPadding(layoutDirection).roundToPx(),
            innerPadding.calculateTopPadding().roundToPx(),
            innerPadding.calculateBottomPadding().roundToPx() +
                    safeAreaPaddingValues.calculateBottomPadding().roundToPx()
        )
    }

    Layout(
        modifier = modifier,
        contents = listOf(
            floatingNavigationIndicator,
            navigationBarBackground,
            navigationBarItems
        )
    ) { (floatingNavigationIndicatorMeasurable,
            navigationBarBackgroundMeasurable,
            navigationBarItemsMeasurables), constraints ->

        val navigationBarBackgroundPlaceable =
            navigationBarBackgroundMeasurable.first().measure(
                constraints.copy(
                    minWidth = constraints.maxWidth,
                    minHeight = constraints.maxHeight
                )
            )

        val navItemsSize = navigationBarItemsMeasurables.size
        val maxContentWidth = constraints.maxWidth - leftPadding - rightPadding
        val maxContentHeight = constraints.maxHeight - topPadding - bottomPadding

        val minNavItemWidth =
            navigationBarItemsMeasurables.minOf { it.minIntrinsicWidth(maxContentHeight) }
        val navItemWidth =
            navigationBarItemsMeasurables.maxOf { it.minIntrinsicWidth(maxContentHeight) }
                .run {
                    return@run if (maxContentWidth < this * navItemsSize) minNavItemWidth else this
                }


        val navigationBarItemsPlaceables = navigationBarItemsMeasurables.map {
            it.measure(constraints.copy(maxWidth = navItemWidth, minWidth = navItemWidth))
        }

        val maxNavItemHeight = navigationBarItemsPlaceables.maxOf { it.height }

        val floatingNavigationIndicatorPlaceable =
            floatingNavigationIndicatorMeasurable.first()
                .measure(
                    constraints.copy(
                        maxWidth = navItemWidth,
                        maxHeight = maxNavItemHeight,
                        minWidth = navItemWidth,
                        minHeight = maxNavItemHeight
                    )
                )

        val finalHeight = topPadding + bottomPadding + maxNavItemHeight
        val navItemWidthAmount = navItemWidth * navItemsSize
        val spaceBetweenItems =
            ((maxContentWidth - navItemWidthAmount) / (navItemsSize + 1)).coerceAtLeast(0)


        layout(width = constraints.maxWidth, height = finalHeight) {
            navigationBarBackgroundPlaceable.placeRelative(0, 0)

            var x = leftPadding + spaceBetweenItems
            var y = topPadding

            floatingNavigationIndicatorPlaceable.placeRelative(
                x,
                y
            )

            navigationBarItemsPlaceables.forEachIndexed { index, navBarItemPlaceable ->
                navBarItemPlaceable.placeRelative(x, y)
                x += spaceBetweenItems + navBarItemPlaceable.width
            }
        }
    }
}