package com.example.fool_tool.ui.util

import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

/** Creates a pair of [MutableInteractionSource], which you can use to emit any
 * actions, and [Modifier], which you can apply to the item of list (e.g. [androidx.compose.foundation.lazy.LazyColumn])
 * specifying it's index.
 *
 * params:
 * [targetIndex] is the only index, which the modifier would be applied to.
 */

@Composable
fun createListItemRippleModifier(targetIndex: Int?): Pair<MutableInteractionSource, Modifier.(listItemIndex: Int) -> Modifier> {
    val interactionSource = remember { MutableInteractionSource() }
    val ripple = remember {
        ripple(
            bounded = true,
        )
    }
    val rippleReminderModifier: Modifier.(listItemIndex: Int) -> Modifier = remember {
        { index ->
            this.then(
                if (index == targetIndex) {
                    Modifier.indication(
                        interactionSource = interactionSource,
                        indication = ripple
                    )
                } else
                    Modifier
            )
        }
    }

    return interactionSource to rippleReminderModifier
}