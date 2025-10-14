package com.example.fool_tool.ui.components.reminder

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fool_tool.R
import com.example.fool_tool.ui.components.shared.AdaptiveSpaceRow
import com.example.fool_tool.ui.model.Reminder
import com.example.fool_tool.ui.model.ReminderStatus
import com.example.fool_tool.ui.theme.FooltoolTheme
import com.example.fool_tool.ui.theme.GREEN_SUCCEED
import com.example.fool_tool.ui.theme.RED_DENIED
import com.example.fool_tool.ui.theme.YELLOW_PENDING
import com.example.fool_tool.ui.theme.onPrimaryDarkHighContrast
import com.example.fool_tool.utils.toFormattedDetailedString
import java.time.LocalDateTime

@Composable
fun ReminderItem(reminder: Reminder, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = dimensionResource(R.dimen.padding_x_small),
            pressedElevation = dimensionResource(R.dimen.padding_small),
            focusedElevation = dimensionResource(R.dimen.padding_small),
            hoveredElevation = dimensionResource(R.dimen.padding_small)
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,

            ),
        border = BorderStroke(
            width = dimensionResource(R.dimen.small_border_width),
            color = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.padding_large),
                vertical = dimensionResource(R.dimen.padding_small)
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AdaptiveSpaceRow(
                    weakComposable = {
                        Text(
                            text = reminder.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    strongComposable = { ReminderStatusIndicator(status = reminder.status) }
                )

            }
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text = reminder.date.toFormattedDetailedString(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                color = MaterialTheme.colorScheme.tertiary
            )
            Spacer(Modifier.height(dimensionResource(R.dimen.padding_small)))
            Text(
                text = reminder.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
    }
}

@Composable
private fun ReminderStatusIndicator(status: ReminderStatus, modifier: Modifier = Modifier) {

    val (color, text) = when (status) {
        ReminderStatus.PENDING -> YELLOW_PENDING to stringResource(R.string.pending)
        ReminderStatus.SUCCEED -> GREEN_SUCCEED to stringResource(R.string.succeed)
        ReminderStatus.DENIED -> RED_DENIED to stringResource(R.string.denied)
    }

    Box(
        modifier = modifier
            .wrapContentWidth()
            .background(color = color, shape = MaterialTheme.shapes.small)
            .padding(dimensionResource(R.dimen.padding_x_small)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            maxLines = 1,
            color = onPrimaryDarkHighContrast,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview
@Composable
fun ReminderItemPreview() {
    FooltoolTheme() {

        ReminderItem(
            reminder = Reminder(
                id = 1,
                date = LocalDateTime.now(),
                title = "Title",
                description = "Description",
                status = ReminderStatus.PENDING
            )
        )
    }
}

@Preview
@Composable
fun ReminderItemDarkPreview() {
    FooltoolTheme(darkTheme = true) {

        ReminderItem(
            reminder = Reminder(
                id = 1,
                date = LocalDateTime.now(),
                title = "Title",
                description = "Description",
                status = ReminderStatus.SUCCEED
            )
        )
    }
}