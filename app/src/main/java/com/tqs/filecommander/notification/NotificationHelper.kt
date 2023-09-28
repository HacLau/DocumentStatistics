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
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

object NotificationHelper {
    val requestUninstallCode = 1011
    val requestServiceCode = 1012
    val requestBatteryCode = 1013
    val requestUnlockCode = 1015

    private val notificationManager by lazy { application.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, important: Int) {
        NotificationChannel(channelId, channelName, important).let {
            it.canBypassDnd()
            it.lockscreenVisibility = Notification.VISIBILITY_SECRET
            it.group = "FileCommander"
            it.canShowBadge()
            it.setBypassDnd(true)
            notificationManager?.createNotificationChannel(it)
        }

    }

    fun createNotificationScheduled(context: Context): Notification? {
        if (NotificationController.notificationControl != 1) {
            return null
        }
        if (NotificationController.isLimit(NotificationKey.UNINSTALL)) {
            return null
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNINSTALL).not()) {
            return null
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, "channel_id_scheduled", "channel_scheduled", NotificationManager.IMPORTANCE_HIGH)
            Notification.Builder(context, "channel_id_scheduled")
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
            setContentTitle(NotificationController.getNotificationName(NotificationKey.SCHEDULED))
            setContentText(NotificationController.getNotificationName(NotificationKey.SCHEDULED))
        }.build()
    }

    fun createNotificationBroadcastUninstall(context: Context) {
        if (NotificationController.notificationControl != 1) {
            return
        }
        if (NotificationController.isLimit(NotificationKey.UNINSTALL)) {
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNINSTALL).not()) {
            return
        }
        notificationManager.notify(
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
                setContentTitle(NotificationController.getNotificationName(NotificationKey.UNINSTALL))
                setContentText(NotificationController.getNotificationName(NotificationKey.UNINSTALL))
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.UNINSTALL)
    }

    fun createNotificationBroadcastCharge(context: Context) {
        if (NotificationController.notificationControl != 1) {
            "notification charge != 1".logE()
            return
        }
        if (NotificationController.isLimit(NotificationKey.CHARGE)) {
            "notification charge limit".logE()
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.CHARGE).not()) {
            "notification charge interval time".logE()
            return
        }
        notificationManager.notify(
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
                setContentTitle(NotificationController.getNotificationName(NotificationKey.CHARGE))
                setContentText(NotificationController.getNotificationName(NotificationKey.CHARGE))
            }.build()
        )
        NotificationController.updateShowTimes(NotificationKey.CHARGE)
    }

    fun createNotificationBroadcastUnlock(context: Context) {
        if (NotificationController.notificationControl != 1) {
            return
        }
        if (NotificationController.isLimit(NotificationKey.UNCLOCK)) {
            return
        }
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNCLOCK).not()) {
            return
        }
        notificationManager.notify(
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
                setContentTitle(NotificationController.getNotificationName(NotificationKey.UNCLOCK))
                setContentText(NotificationController.getNotificationContent(NotificationKey.UNCLOCK))
            }.build()
        )

        NotificationController.updateShowTimes(NotificationKey.UNCLOCK)
    }

    private fun getIntent(context: Context): Intent {
        return Intent(context, AdsOpenActivity::class.java)
    }
}