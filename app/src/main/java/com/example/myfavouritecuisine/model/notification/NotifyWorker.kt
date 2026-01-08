package com.example.myfavouritecuisine.model.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.createBitmap
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myfavouritecuisine.R
import com.example.myfavouritecuisine.utils.Constants
import com.example.myfavouritecuisine.view.activities.MainActivity

class NotifyWorker(context: Context, workerParams: WorkerParameters): Worker(context = context, workerParams = workerParams) {
    override fun doWork(): Result {
        Log.d("mohnishUriCheck", "doWork is called")
        sendNotification()
        Log.d("mohnishUriCheck", "doWork is completed")
        return Result.success()
    }

    private fun sendNotification() {
        val notification_id = 0
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(Constants.NOTIFICATION_ID, notification_id)

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val title = applicationContext.getString(R.string.notification_title)
        val subtitle = applicationContext.getString(R.string.notification_subtitle)

        val bitmap = applicationContext.vectorToBitmap(R.drawable.ic_launcher_foreground)

        val bigPicStyle = NotificationCompat.BigPictureStyle()
            .bigPicture(bitmap)
            .bigLargeIcon(null as Bitmap?)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val ringtoneManager = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = android.media.AudioAttributes.Builder()
                .setUsage(android.media.AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(android.media.AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL,
                Constants.NOTIFICATION_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            channel.setSound(ringtoneManager, audioAttributes)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_notifications_black_24dp)
            .setLargeIcon(bitmap)
            .setContentTitle(title)
            .setContentText(subtitle)
            .setStyle(bigPicStyle)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .build()

        notificationManager.notify(notification_id, notification)

    }

    private fun Context.vectorToBitmap(drawableId: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(this,drawableId) ?: return null
        val bitmap = createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}
