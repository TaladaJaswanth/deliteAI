/*
 * SPDX-FileCopyrightText: (C) 2025 DeliteAI Authors
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package dev.deliteai.notifications_summarizer.impl.broadcast

import dev.deliteai.notifications_summarizer.dataModels.NotificationSummarizerConfig
import dev.deliteai.notifications_summarizer.impl.common.Constants.ACTION_NOTIFICATION_SUMMARIZER_ALARM
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent

class AlarmBroadcastScheduler(
    private val application: Application,
    private val alarmManager: AlarmManager,
    private val config: NotificationSummarizerConfig
) {
    fun scheduleNotificationsSummaryAlarm(timeInMillis: Long) {
        val alarmIntent = buildAlarmPendingIntent()
        alarmManager.cancel(alarmIntent)

        alarmManager.setAlarmClock(
            AlarmManager.AlarmClockInfo(timeInMillis, alarmIntent),
            alarmIntent
        )
    }

    private fun buildAlarmPendingIntent(): PendingIntent {
        val intent = Intent(application, AlarmBroadcastReceiver::class.java).apply {
            action = ACTION_NOTIFICATION_SUMMARIZER_ALARM
            addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES or Intent.FLAG_RECEIVER_FOREGROUND)
        }

        return PendingIntent.getBroadcast(
            application,
            17641,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
    }
}
