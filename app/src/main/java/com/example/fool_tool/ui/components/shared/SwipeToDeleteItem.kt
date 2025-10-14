package com.example.fool_tool.ui.components.shared

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.reminder.ReminderItem
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.ui.theme.FooltoolTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.math.absoluteValue


@Composable
fun SwipeToDeleteItem(
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    threshold: Dp = 80.dp,
    element: @Composable () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    val density = LocalDensity.current

    val thresholdDistancePx = remember(density, threshold) {
        -with(density) { threshold.toPx() }.absoluteValue
    }
    val translationX = remember { Animatable(0f) }

    var isDialogShown by remember { mutableStateOf(false) }

    val draggableState =
        rememberDraggableState { delta ->
            coroutineScope.launch {
                translationX.snapTo(delta + translationX.value)
            }
        }

    LaunchedEffect(Unit) {
        translationX.updateBounds(thresholdDistancePx, 0f)
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.CenterEnd,
            modifier = Modifier
                .matchParentSize()
                .clip(RoundedCornerShape(size = dimensionResource(R.dimen.medium_shape_radius)))
                .graphicsLayer {
                    this.alpha = if (translationX.value == 0f) 0f else 1f
                }
                .background(color = MaterialTheme.colorScheme.errorContainer),
        ) {

            Icon(
                painter = painterResource(R.drawable.ic_trash_can),
                contentDescription = stringResource(R.string.ensure_deletion_question),
                tint = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier
                    .offset(x = dimensionResource(R.dimen.default_icon_size) / 2)
                    .graphicsLayer {
                        this.translationX = translationX.value / 2
                    }
            )


        }
        Row(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .graphicsLayer {
                    this.translationX = translationX.value
                }
                .draggable(
                    state = draggableState,
                    orientation = Orientation.Horizontal,
                    onDragStopped = {
                        if (translationX.value <= thresholdDistancePx) {
                            isDialogShown = true
                        }
                        coroutineScope.launch {
                            translationX.animateTo(targetValue = 0f, animationSpec = spring())
                        }
                    }
                )
        ) {
            element()
        }

        if (isDialogShown) {
            DeleteItemAlertDialog(
                onDismiss = { isDialogShown = false },
                onConfirm = { onDelete() },
            )

        }
    }
}


@Preview
@Composable
private fun HorizontalGestureDeletingElementPreview() {
    FooltoolTheme {
        SwipeToDeleteItem(onDelete = {}) {
            ReminderItem(
                reminder = Reminder(
                    id = 1,
                    date = LocalDateTime.now(),
                    title = "Title",
                    description = "Description",
                    status = ReminderStatus.PENDING
                ), modifier = Modifier.alpha(0.5f)
            )
        }

    }
}

