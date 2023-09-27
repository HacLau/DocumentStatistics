package com.tqs.filecommander.notification

import com.blankj.utilcode.util.TimeUtils
import com.tqs.filecommander.mmkv.MMKVHelper

object NotificationController {

    var notificationControl = 1
    private var notificationMap: MutableMap<String, NotificationItem> = mutableMapOf()
    private var showedMap: MutableMap<String, NotificationShowed> = mutableMapOf()
    fun setNotification(notificationEntity: NotificationEntity) {
        notificationControl = notificationEntity.notificationSwitch
        notificationMap.clear()
        notificationMap[NotificationKey.TIMING] = notificationEntity.timing
        notificationMap[NotificationKey.UNCLOCK] = notificationEntity.unclock
        notificationMap[NotificationKey.UNINSTALL] = notificationEntity.uninstall
        notificationMap[NotificationKey.BATTERY] = notificationEntity.battery

        showedMap[NotificationKey.TIMING] = MMKVHelper.normalNotification as NotificationShowed
        showedMap[NotificationKey.UNCLOCK] = MMKVHelper.unlockNotification as NotificationShowed
        showedMap[NotificationKey.UNINSTALL] = MMKVHelper.uninstallNotification as NotificationShowed
        showedMap[NotificationKey.BATTERY] = MMKVHelper.batteryNotification as NotificationShowed

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
        return when (notificationMap[key]?.dayShowLimit) {
            0 -> false
            showedMap[key]?.showTimes -> true
            else -> false
        }
    }

    fun isMoreIntervalTime(key: String): Boolean {
        return System.currentTimeMillis() - showedMap[key]?.lastShowTime!! >
                if (showedMap[key]?.showTimes == 0) {
                    notificationMap[key]?.delayPopupTime!!
                } else {
                    notificationMap[key]?.intervalPopupTime!!
                } * 1000 * 60

    }

    fun isOpenPopup(key:String):Boolean{
        return notificationMap[key]?.isPopup == 1
    }

    private fun updateMMKVNotification(key: String) {
        showedMap[key]?.let {
            when (key) {
                NotificationKey.TIMING -> MMKVHelper.normalNotification = it
                NotificationKey.UNCLOCK -> MMKVHelper.unlockNotification = it
                NotificationKey.UNINSTALL -> MMKVHelper.uninstallNotification = it
                NotificationKey.BATTERY -> MMKVHelper.batteryNotification = it
            }
        }
    }
}