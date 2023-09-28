package com.tqs.filecommander.mmkv

import com.tqs.filecommander.ads.AdsCount
import com.tqs.filecommander.notification.NotificationShowed

object MMKVHelper : MMKVOwner(mapId = "fileCommander") {
    // is request read/write and storage permissions
    var requestPermission by mmkvBoolean(default = false)
    var requestCodeManager by mmkvBoolean(default = false)
    // advertising showing times and click times
    var showAdsData by mmkvParcelable<AdsCount>()
    var clickAdsData by mmkvParcelable<AdsCount>()
    // APP main activity showed time
    var showMainActivityTime by mmkvLong(default = 0L)

    //referrer
    var installReferrer by mmkvString(default = "")
    var installReferrerVersion by mmkvString(default = "")
    var referrerClickTimestampSeconds by mmkvLong(default = 0L)
    var referrerClickTimestampServerSeconds by mmkvLong(default = 0L)
    var installBeginTimestampSeconds by mmkvLong(default = 0L)
    var installBeginTimestampServerSeconds by mmkvLong(default = 0L)
    var googlePlayInstantParam by mmkvBoolean(default = false)

    // first launch app
    var firstLaunchApp by mmkvBoolean(default = true)
    // cloak state
    var cloakState by mmkvString(default = "")
    // notification
    var normalNotification by mmkvParcelable<NotificationShowed>(default = NotificationShowed())
    var uninstallNotification by mmkvParcelable<NotificationShowed>(default = NotificationShowed())
    var batteryNotification by mmkvParcelable<NotificationShowed>(default = NotificationShowed())
    var unlockNotification by mmkvParcelable<NotificationShowed>(default = NotificationShowed())
    // adsCost
    var currentUserCost by mmkvDouble(default = 0.0)
    var currentUserCostTime by mmkvLong(default = System.currentTimeMillis())
    var androidId by mmkvString(default = "")
}