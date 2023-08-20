package com.asustug.ServerAIDL

import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import org.jetbrains.annotations.Nullable


class AIDLServerService : Service() {

    companion object{
        @JvmField
        public var isServiceRunning = false
    }

    private val TAG = "AIDLServerService"

    private val CHANNEL_ID = "NOTIFICATION_CHANNEL"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel();
        isServiceRunning = true;
    }

    @Nullable
    override fun onBind(p0: Intent?): IBinder {
        Log.d(TAG, "binder called")
        return binder
    }

    private val binder: IMathAidlInterface.Stub = object : IMathAidlInterface.Stub() {
        override fun add(a: Int, b: Int): Int {
            return a + b
        }

        override fun sub(a: Int, b: Int): Int {
            return a - b
        }

        override fun multiply(a: Int, b: Int): Int {
            return a * b
        }

        override fun divide(a: Int, b: Int): Int {
            return a / b
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val appName = "ServerAIDL"
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                appName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand called")
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, PendingIntent.FLAG_IMMUTABLE
        )
        val notification: Notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Service is Running")
            .setContentText("Listening for Screen Off/On events")
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentIntent(pendingIntent)
            .setColor(getColor(R.color.holo_blue_light))
            .build()
        startForeground(1, notification)
        return START_STICKY
    }
}