package com.example.fool_tool.ui.components.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.fool_tool.R
import com.example.fool_tool.ui.screens.reminder.PermissionsUiState

@Composable
fun PermissionsBlock(
    permissionsUiState: PermissionsUiState,
    grantNotificationPermission: () -> Unit,
    grantNotificationChannelPermission: () -> Unit,
    grantAlarmPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium)),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.permissions_warning_title),
            textAlign = TextAlign.Center
        )
        with(permissionsUiState) {
            if (!alarm) {
                Text(
                    text = stringResource(R.string.permission_alarms_warning_description),
                    textAlign = TextAlign.Center
                )
                Button(onClick = grantAlarmPermission) {
                    Text(text = stringResource(R.string.to_settings))
                }
            }
            if (!notification) {
                Text(
                    text = stringResource(R.string.permission_notification_warning_description),
                    textAlign = TextAlign.Center
                )
                Button(onClick = grantNotificationPermission) {
                    Text(text = stringResource(R.string.to_settings))
                }

            } else {
                if (!notificationChannel) {
                    Text(
                        text = stringResource(R.string.permission_notification_channel_warning_description),
                        textAlign = TextAlign.Center
                    )
                    Button(onClick = grantNotificationChannelPermission) {
                        Text(text = stringResource(R.string.to_settings))
                    }
                }
            }
        }


    }

}