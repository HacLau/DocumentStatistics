package com.tqs.filecommander.cloak

import android.os.Build
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.HttpHelper
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.encode
import com.tqs.filecommander.utils.getAndroidId
import org.json.JSONObject

object CloakHelper {
    private var cloakState = ""
    private var reloadTimes = 0
    fun getCloakConfig() {
        HttpHelper.sendRequestGet(jsonObject = getCloakJsonConfig(), resultSuccess = {
            cloakState = it
            MMKVHelper.cloakState = it
            reloadTimes = 0
        }, resultFailed = { code, message ->
            if (reloadTimes < 20)
                getCloakConfig()
        })
    }

    private fun getCloakJsonConfig(): JSONObject {
        return JSONObject().apply {
            put(CloakKey.sofia, getAndroidId().encode())
            put(CloakKey.fusty, System.currentTimeMillis().toString().encode())
            put(CloakKey.stubby, Build.MODEL.encode())
            put(CloakKey.noodle, BuildConfig.APPLICATION_ID.encode())
            put(CloakKey.ohio, Build.VERSION.RELEASE.encode())
//            put(CloakKey.previous,"")
            put(CloakKey.tabletop, TBAHelper.getGAId())
            put(CloakKey.abbey, getAndroidId().encode())
            put(CloakKey.animist, "twigging".encode())
//            put(CloakKey.bell, "")
            put(CloakKey.rib, BuildConfig.VERSION_NAME.encode())
//            put(CloakKey.crib,"")
//            put(CloakKey.stab,"")
//            put(CloakKey.gondola,"")
        }
    }

}