package com.example.fool_tool.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fool_tool.data.use_cases.RemindersProcessingUseCase
import com.example.fool_tool.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class BootCompletedReceiver() : BroadcastReceiver() {

    @Inject
    lateinit var remindersProcessingUseCase: RemindersProcessingUseCase

    override fun onReceive(context: Context?, intent: Intent?) = goAsync { pendingResult ->
        if (context != null && intent != null) {
            if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
                remindersProcessingUseCase.reactivateOrDeclinePendingReminders()
            }
        }
    }
}