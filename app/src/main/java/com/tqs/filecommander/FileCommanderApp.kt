package com.tqs.filecommander

import android.app.Application
import com.tencent.mmkv.MMKV
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.ads.initReferrer
import com.tqs.filecommander.vm.utils.*

class FileCommanderApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val adsJson = getJsonFromAssets(this, "ads.json")
        AdsManager.initAdsConfig(adsJson)
        MMKV.initialize(this)
        initReferrer(this)
        application = this
    }
}