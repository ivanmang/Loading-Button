package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R
import com.udacity.Status

/**
 * Builds and delivers a notification.
 *
 * @param messageBody, notification text.
 * @param context, activity context.
 *
 */

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(fileName:String, status:Status,  messageBody: String, applicationContext: Context) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)

    contentIntent.putExtra("filename", fileName)
    contentIntent.putExtra("status", status.value)

    val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            NOTIFICATION_ID,
            contentIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
            applicationContext,
            MainActivity.CHANNEL_ID
    )
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(applicationContext.getString(R.string.notification_title))
            .setContentText(messageBody)

            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .addAction(
                    R.drawable.ic_launcher_foreground,
                    applicationContext.getString(R.string.notification_button),
                    contentPendingIntent
            )

    notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
    cancelAll()
}