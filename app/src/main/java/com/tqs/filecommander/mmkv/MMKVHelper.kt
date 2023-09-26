package com.tqs.filecommander.mmkv

import com.tqs.filecommander.ads.AdsCount

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
}