package com.example.fool_tool.receivers

import android.app.AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.core_android.extensions.goAsync
import com.example.fool_tool.domain.use_case.ReactivateOrDeclinePendingRemindersUseCase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmPermissionReceiver() : BroadcastReceiver() {
    @Inject
    lateinit var reactivateOrDeclinePendingRemindersUseCase: ReactivateOrDeclinePendingRemindersUseCase

    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
        if (context != null && intent != null) {
            if (intent.action == ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                reactivateOrDeclinePendingRemindersUseCase()
            }
        }
    }
}
