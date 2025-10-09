package com.example.fool_tool.ui.components.shared

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** This composable is a [androidx.compose.ui.layout.Layout] that gives the [strongComposable]
 * all space it needs.
 * As for [weakComposable], it is provided with as much width as it needs until it
 * reaches all free space, and the [minGapBetween] will take it's minimum and shrink if other
 * composables allow it. [isWeakFirst] indicates whether the weak composable should be shown first
 */

@Composable
fun AdaptiveSpaceRow(
    weakComposable: @Composable () -> Unit,
    strongComposable: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    isWeakFirst: Boolean = true,
    minGapBetween: Dp = 6.dp,
) {
    Layout(
        content = {
            weakComposable()
            strongComposable()
        },
        modifier = modifier
    ) { measurables, constraints ->
        val totalWidth = constraints.maxWidth

        val weakMeasurable = measurables[0]
        val strongMeasurable = measurables[1]

        val strongPlaceable = strongMeasurable.measure(constraints)

        val weakCompMaxWidth = totalWidth - strongPlaceable.width - minGapBetween.roundToPx()
        val weakPlaceable = weakMeasurable.measure(constraints.copy(maxWidth = weakCompMaxWidth))

        val gap = weakCompMaxWidth - weakPlaceable.width

        val firstPlaceable = if (isWeakFirst) weakPlaceable else strongPlaceable
        val secondPlaceable = if (isWeakFirst) strongPlaceable else weakPlaceable


        val maxHeight = maxOf(firstPlaceable.height, secondPlaceable.height)

        val firstPlaceableOffsetY = (maxHeight - firstPlaceable.height) / 2
        val secondPlaceableOffsetY = (maxHeight - secondPlaceable.height) / 2

        layout(width = totalWidth, height = maxHeight) {
            firstPlaceable.placeRelative(x = 0, y = firstPlaceableOffsetY)
            secondPlaceable.placeRelative(
                x = gap + firstPlaceable.width,
                y = secondPlaceableOffsetY
            )
        }
    }
}