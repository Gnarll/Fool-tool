package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.example.fool_tool.R
import com.example.fool_tool.utils.ValidationError
import java.time.LocalDateTime

@Composable
fun DateTimePicker(
    dateTime: LocalDateTime,
    onDateTimeChanged: (LocalDateTime) -> Unit,
    error: ValidationError?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.padding_medium)
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                dimensionResource(R.dimen.padding_medium)
            )
        ) {
            DatePickerDocked(
                onDatePicked = { date ->
                    val newDate = dateTime.with(date)
                    onDateTimeChanged(newDate)
                },
                date = dateTime.toLocalDate(),
                modifier = Modifier.weight(1f)
            )
            DialTimePicker(
                onTimePicked = { time ->
                    val newDate = dateTime.with(time)
                    onDateTimeChanged(newDate)
                },
                time = dateTime.toLocalTime(),
                modifier = Modifier.weight(1f)
            )
        }
        error?.let {
            Text(
                text = it.errorMessage.asString(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}