package com.tqs.filecommander.notification

import android.content.Context
import com.blankj.utilcode.util.TimeUtils
import com.google.gson.Gson
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets
import com.tqs.filecommander.utils.logE

object NotificationController {

    var notificationControl = 0
    private var notificationMap: MutableMap<String, NotificationItem> = mutableMapOf()
    private var showedMap: MutableMap<String, NotificationShowed> = mutableMapOf()
    fun setNotification(notificationEntity: NotificationEntity) {
        notificationControl = notificationEntity.notificationSwitch
        notificationMap.clear()
        notificationMap[NotificationKey.SCHEDULED] = notificationEntity.timing
        notificationMap[NotificationKey.UNCLOCK] = notificationEntity.unclock
        notificationMap[NotificationKey.UNINSTALL] = notificationEntity.uninstall
        notificationMap[NotificationKey.CHARGE] = notificationEntity.battery

        initNotificationConfig(application)
    }

    fun initNotificationConfig(context: Context) {
        if (showedMap.isNotEmpty()) return
        Gson().fromJson<NotificationConfig>(
            getJsonFromAssets(context, "notification.json"),
            NotificationConfig::class.java
        ).apply {
            for (item in this.scenes) {
                val key = when (item.name) {
                    NotificationKey.Scheduled -> {
                        MMKVHelper.normalNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.SCHEDULED
                    }

                    NotificationKey.Unlock -> {
                        MMKVHelper.unlockNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.UNCLOCK
                    }

                    NotificationKey.Uninstall -> {
                        MMKVHelper.uninstallNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.UNINSTALL
                    }

                    NotificationKey.Charge -> {
                        MMKVHelper.batteryNotification = NotificationShowed().apply {
                            title = item.name
                            content = item.notification
                        }
                        NotificationKey.CHARGE
                    }

                    else -> {
                        ""
                    }
                }
                updateShowedNotification(key)
//                updateMMKVNotification(key)

            }
        }
    }

    private fun updateShowedNotification(key: String) {
        when(key){
            NotificationKey.SCHEDULED -> showedMap[key] = MMKVHelper.normalNotification as NotificationShowed
            NotificationKey.UNCLOCK -> showedMap[key] = MMKVHelper.unlockNotification as NotificationShowed
            NotificationKey.UNINSTALL -> showedMap[key] = MMKVHelper.uninstallNotification as NotificationShowed
            NotificationKey.CHARGE -> showedMap[key] = MMKVHelper.batteryNotification as NotificationShowed
        }
    }

    private fun updateMMKVNotification(key: String) {
        showedMap[key]?.let {
            when (key) {
                NotificationKey.SCHEDULED -> MMKVHelper.normalNotification = it
                NotificationKey.UNCLOCK -> MMKVHelper.unlockNotification = it
                NotificationKey.UNINSTALL -> MMKVHelper.uninstallNotification = it
                NotificationKey.CHARGE -> MMKVHelper.batteryNotification = it
            }
        }
    }

    fun updateShowTimes(key: String) {
        showedMap[key]?.let {
            if (TimeUtils.isToday(it.lastShowTime)) {
                it.showTimes++
            } else {
                it.showTimes = 1
            }
            it.lastShowTime = System.currentTimeMillis()
        }
        updateMMKVNotification(key)
    }

    fun isLimit(key: String): Boolean {
        "notification = ${notificationMap[key]?.dayShowLimit}".logE()
        return notificationMap[key]?.let {
            when (it.dayShowLimit) {
                0 -> false
                showedMap[key]?.showTimes -> true
                null -> false
                else -> false
            }
        }?:false
    }

    fun isMoreIntervalTime(key: String): Boolean {
        return System.currentTimeMillis() - showedMap[key]?.lastShowTime!! >
                if (showedMap[key]?.showTimes == 0) {
                    notificationMap[key]?.delayPopupTime!!
                } else {
                    notificationMap[key]?.intervalPopupTime!!
                } * 1000 * 60

    }

    fun isOpenPopup(key: String): Boolean {
        return notificationMap[key]?.isPopup == 1
    }

    fun getNotificationName(key: String): String? {
        return showedMap[key]?.title
    }


    fun getNotificationContent(key: String): String? {
        return showedMap[key]?.content
    }
}