package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.util.cancelNotifications
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_detail.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    var fileName = ""
    var status = Status.FAIL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
        ) as NotificationManager

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {
            notificationManager.cancelNotifications()
            when {
                glide_button.isChecked -> {
                    URL = "https://github.com/bumptech/glide"
                    fileName = getString(R.string.glide_button_text)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                }
                load_app_button.isChecked -> {
                    URL = "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter"
                    fileName = getString(R.string.loadingapp_button_text)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                }
                retrofit_button.isChecked -> {
                    URL = "https://github.com/square/retrofit"
                    fileName = getString(R.string.retrofit_button_text)
                    download()
                    custom_button.buttonState = ButtonState.Loading
                } else -> {
                    Toast.makeText(applicationContext, applicationContext.getText(R.string.please_select), Toast.LENGTH_SHORT).show()
                }
            }

        }

        createChannel(
                CHANNEL_ID,
                getString(R.string.notification_channel_name)
        )
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadID == id) {
                custom_button.buttonState = ButtonState.Completed
                status = Status.SUCCESS
            }
            notificationManager.sendNotification(fileName, status, applicationContext.getString(R.string.notification_description), applicationContext)
        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(URL))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private var URL =
            ""
        const val CHANNEL_ID = "channelId"
    }

    private fun createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            )

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download Complete"

            notificationManager = this.getSystemService(
                    NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(notificationChannel)
        }



    }


}
