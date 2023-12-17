package com.example.myapplication
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyService : Service() {
    private val CHANNEL_ID = "MyServiceChannel"
    private lateinit var notificationManager: NotificationManager
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        handler = Handler()
        runnable = Runnable {
            sendNotification()
            handler.postDelayed(runnable, 10 * 60 * 1000)
        }
        Log.d("MyService", "Service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        handler.postDelayed(runnable, 10 * 60 * 1000)
        Log.d("MyService", "Service started")
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
        Log.d("MyService", "Service destroyed")
    }

    private fun sendNotification() {
        val notificationIntent = Intent(this, MyService::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val actionIntent = Intent(this, MyService::class.java)
        actionIntent.action = "STOP_SERVICE"
        val stopServicePendingIntent = PendingIntent.getService(
            this,
            0,
            actionIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Service")
            .setContentText("Notification every 10 minutes")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentIntent(pendingIntent)
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "Stop Service",
                stopServicePendingIntent
            )
            .build()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "My Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(1, notification)
        Log.d("MyService", "Notification sent")
    }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
        if (intent?.action != null && intent.action == "STOP_SERVICE") {
            stopSelf()
        }
        Log.d("MyService", "OnStart")
    }
}
