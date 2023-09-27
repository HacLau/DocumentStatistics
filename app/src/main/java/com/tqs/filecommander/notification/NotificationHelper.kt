package com.tqs.filecommander.notification

import android.app.Activity
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import com.tqs.filecommander.R
import com.tqs.filecommander.ui.activity.AdsOpenActivity

object NotificationHelper {
    val requestUninstallCode = 1011
    val requestServiceCode = 1012
    val requestBatteryCode = 1013
    val requestUnlockCode = 1015

    var notificationManager: NotificationManager? = null

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, important: Int) {
        NotificationChannel(channelId, channelName, important).let {
            it.canBypassDnd()
            it.lockscreenVisibility = Notification.VISIBILITY_SECRET
            it.canShowBadge()
            it.setBypassDnd(true)
            notificationManager?.createNotificationChannel(it)
        }

    }


    fun createNotificationService(context: Context): Notification? {
        if (NotificationController.notificationControl != 1) {
            return null
        }
        getManager(context)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, "channel_id_service", "channel_service", NotificationManager.IMPORTANCE_HIGH)
            Notification.Builder(context, "channel_id_service")
        } else {
            Notification.Builder(context)
        }.apply {
            PendingIntent.getActivity(context, requestServiceCode, getIntent(context), PendingIntent.FLAG_IMMUTABLE).let {
                setContentIntent(it)
            }
            setAutoCancel(true)
            setSmallIcon(R.mipmap.ic_launcher_foreground)
            setWhen(System.currentTimeMillis())
            setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
            setContentTitle("Service")
            setContentText("a service")
        }.build()
    }

    fun createNotificationBroadcastUninstall(context: Context) {
        if (NotificationController.notificationControl != 1) {
            return
        }
        if (NotificationController.isLimit(NotificationKey.UNINSTALL)) {
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNINSTALL).not()){
            return
        }
        getManager(context)
        notificationManager?.notify(
            requestUninstallCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_uninstall", "channel_uninstall", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_uninstall")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(PendingIntent.getActivity(context, requestUninstallCode, getIntent(context), PendingIntent.FLAG_IMMUTABLE))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
                setContentTitle("Uninstall")
                setContentText("a uninstall")
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.UNINSTALL)
    }

    fun createNotificationBroadcastBattery(context: Context) {
        if (NotificationController.notificationControl != 1) {
            return
        }
        if (NotificationController.isLimit(NotificationKey.BATTERY)) {
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.BATTERY).not()){
            return
        }
        getManager(context)
        notificationManager?.notify(
            requestBatteryCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_battery", "channel_battery", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_battery")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(PendingIntent.getActivity(context, requestBatteryCode, getIntent(context), PendingIntent.FLAG_IMMUTABLE))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
                setContentTitle("Battery")
                setContentText("a battery")
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.BATTERY)
    }

    fun createNotificationBroadcastUnlock(context: Context) {
        if (NotificationController.notificationControl != 1) {
            return
        }
        if (NotificationController.isLimit(NotificationKey.UNCLOCK)) {
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNCLOCK).not()){
            return
        }
        getManager(context)
        notificationManager?.notify(
            requestUnlockCode,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(context, "channel_id_unlock", "channel_unlock", NotificationManager.IMPORTANCE_HIGH)
                Notification.Builder(context, "channel_id_unlock")
            } else {
                Notification.Builder(context)
            }.apply {
                setContentIntent(PendingIntent.getActivity(context, requestUnlockCode, getIntent(context), PendingIntent.FLAG_IMMUTABLE))
                setAutoCancel(true)
                setSmallIcon(R.mipmap.ic_launcher_foreground)
                setWhen(System.currentTimeMillis())
                setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher_foreground))
                setContentTitle("Unlock")
                setContentText("a unlock")
            }.build()
        )

        NotificationController.updateShowTimes(NotificationKey.UNCLOCK)
    }

    private fun getManager(context: Context) {
        if (notificationManager == null)
            notificationManager = context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
    }

    private fun getIntent(context: Context): Intent {
        return Intent(context, AdsOpenActivity::class.java).apply {
//            putExtra("flag","")
        }
    }
}