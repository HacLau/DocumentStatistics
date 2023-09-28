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
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.ui.activity.AdsOpenActivity
import com.tqs.filecommander.utils.application

object NotificationHelper {
    const val requestUninstallCode = 1011
    const val requestServiceCode = 1012
    const val requestBatteryCode = 1013
    const val requestUnlockCode = 1015

    private val notificationManager by lazy { application.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, important: Int) {
        NotificationChannel(channelId, channelName, important).let {
            it.canBypassDnd()
            it.lockscreenVisibility = Notification.VISIBILITY_SECRET
            it.group = "FileCommander"
            it.canShowBadge()
            it.setBypassDnd(true)
            notificationManager.createNotificationChannel(it)
        }

    }

    fun createNotificationScheduled(context: Context): Notification? {
        if (ReferrerHelper.isReferrerUser().not()) return null
        if (NotificationController.notificationControl != 1) return null
        if (NotificationController.isLimit(NotificationKey.UNINSTALL)) return null
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNINSTALL).not()) return null
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
        if (ReferrerHelper.isReferrerUser().not()) return
        if (NotificationController.notificationControl != 1) return
        if (NotificationController.isLimit(NotificationKey.UNINSTALL))return
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNINSTALL).not()) return
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
        if (ReferrerHelper.isReferrerUser().not()) return
        if (NotificationController.notificationControl != 1) return
        if (NotificationController.isLimit(NotificationKey.CHARGE)) return
        if (NotificationController.isMoreIntervalTime(NotificationKey.CHARGE).not())return
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
        if (ReferrerHelper.isReferrerUser().not()) return
        if (NotificationController.notificationControl != 1) return
        if (NotificationController.isLimit(NotificationKey.UNCLOCK)) return
        if (NotificationController.isMoreIntervalTime(NotificationKey.UNCLOCK).not()) return
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