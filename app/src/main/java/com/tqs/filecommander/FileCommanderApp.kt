package com.tqs.filecommander

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.referrer.ReferrerHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getJsonFromAssets

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val adsJson = getJsonFromAssets(this, "ads.json")
        AdsManager.initAdsConfig(adsJson)
        MMKV.initialize(this)
        ReferrerHelper.initReferrer(this)
        application = this

    }
}