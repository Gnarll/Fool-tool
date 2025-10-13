package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextOverflow
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
        modifier = modifier.fillMaxWidth()
    ) { measurables, constraints ->
        val maxWidth = constraints.maxWidth

        val weakMeasurable = measurables[0]
        val strongMeasurable = measurables[1]

        val strongPlaceable = strongMeasurable.measure(constraints.copy(minWidth = 0))

        var weakPlaceable: Placeable
        var gap: Int

        if (constraints.hasBoundedWidth) {
            val weakAndGapRestWidth =
                (maxWidth - strongPlaceable.width - minGapBetween.roundToPx()).coerceAtLeast(0)

            weakPlaceable =
                weakMeasurable.measure(
                    constraints.copy(
                        maxWidth = weakAndGapRestWidth,
                        minWidth = 0
                    )
                )

            gap = weakAndGapRestWidth - weakPlaceable.width
        } else {
            weakPlaceable = weakMeasurable.measure(constraints)
            gap = minGapBetween.roundToPx()
        }

        val totalWidth = strongPlaceable.width + weakPlaceable.width + gap

        val firstPlaceable = if (isWeakFirst) weakPlaceable else strongPlaceable
        val secondPlaceable = if (isWeakFirst) strongPlaceable else weakPlaceable

        val totalHeight = maxOf(firstPlaceable.height, secondPlaceable.height)

        val firstY = (totalHeight - firstPlaceable.height) / 2
        val secondY = (totalHeight - secondPlaceable.height) / 2

        layout(width = totalWidth, height = totalHeight) {
            firstPlaceable.placeRelative(x = 0, y = firstY)
            secondPlaceable.placeRelative(
                x = gap + firstPlaceable.width,
                y = secondY
            )
        }
    }
}