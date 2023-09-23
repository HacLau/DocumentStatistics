package com.tqs.filecommander.mmkv

import com.tqs.filecommander.ads.AdsCount

object MMKVHelper : MMKVOwner(mapId = "fileCommander") {
    var requestPermission by mmkvBoolean(default = false)
    var requestCodeManager by mmkvBoolean(default = false)
    var showAdsData by mmkvParcelable<AdsCount>()
    var clickAdsData by mmkvParcelable<AdsCount>()
    var showMainActivityTime by mmkvLong(default = 0L)

    //referrer
    var installReferrer by mmkvString(default = "")
    var installReferrerVersion by mmkvString(default = "")
    var referrerClickTimestampSeconds by mmkvLong(default = 0L)
    var referrerClickTimestampServerSeconds by mmkvLong(default = 0L)
    var installBeginTimestampSeconds by mmkvLong(default = 0L)
    var installBeginTimestampServerSeconds by mmkvLong(default = 0L)
    var googlePlayInstantParam by mmkvBoolean(default = false)
}