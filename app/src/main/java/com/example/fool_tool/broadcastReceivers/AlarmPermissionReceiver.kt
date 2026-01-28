package com.example.fool_tool.broadcastReceivers

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.use_cases.RemindersProcessingUseCase
import com.example.fool_tool.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmPermissionReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var remindersProcessingUseCase: RemindersProcessingUseCase

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context != null && intent != null) {
            if (intent.action == ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                remindersProcessingUseCase.reactivateOrDeclinePendingReminders()
            }
        }
    }
}
