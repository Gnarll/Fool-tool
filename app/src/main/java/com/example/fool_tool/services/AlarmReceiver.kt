package com.example.fool_tool.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.alarm.AndroidAlarmScheduler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra(AndroidAlarmScheduler.EXTRA_TITLE) ?: return
        val description = intent.getStringExtra(AndroidAlarmScheduler.EXTRA_TITLE) ?: return

    }
}