package com.tqs.filecommander.notification

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

object NotificationHelper {

    var notificationControl = 1
    var notificationMap: MutableMap<String,NotificationItem> = mutableMapOf()
    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(context: Context, channelId: String, channelName: String, important: Int) {
        val manager = context.getSystemService(Activity.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(NotificationChannel(channelId,channelName,important))
    }

    fun setNotification(notificationEntity: NotificationEntity) {
        notificationControl = notificationEntity.notificationSwitch
        notificationMap[NotificationKey.TIMING] = notificationEntity.timing
        notificationMap[NotificationKey.UNCLOCK] = notificationEntity.unclock
        notificationMap[NotificationKey.UNLOAD] = notificationEntity.unload
        notificationMap[NotificationKey.CHARGE] = notificationEntity.charge
    }
}