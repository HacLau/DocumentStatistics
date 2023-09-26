package com.tqs.filecommander.cloak

import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.HttpHelper
import com.tqs.filecommander.utils.application
import org.json.JSONObject
import java.net.URLEncoder

object CloakHelper {
    private var cloakState = ""
    fun getCloakConfig() {
        HttpHelper.sendRequestGet(jsonObject = getCloakJsonConfig(), resultSuccess = {
            cloakState = it
            MMKVHelper.cloakState = it
        }, resultFailed = { code, message ->

        })
    }

    @SuppressLint("HardwareIds")
    private fun getCloakJsonConfig(): JSONObject {
        return JSONObject().apply {
            put(CloakKey.sofia, URLEncoder.encode(Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID), "UTF-8"))
            put(CloakKey.fusty, URLEncoder.encode(System.currentTimeMillis().toString(), "UTF-8"))
            put(CloakKey.stubby, URLEncoder.encode(Build.MODEL, "UTF-8"))
            put(CloakKey.noodle, URLEncoder.encode(BuildConfig.APPLICATION_ID, "UTF-8"))
            put(CloakKey.ohio, URLEncoder.encode(Build.VERSION.RELEASE, "UTF-8"))
//            put(CloakKey.previous,"")
            put(CloakKey.tabletop, URLEncoder.encode("", "UTF-8"))
            put(CloakKey.abbey, URLEncoder.encode(Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID), "UTF-8"))
            put(CloakKey.animist, URLEncoder.encode("twigging", "UTF-8"))
            put(CloakKey.bell, "")
            put(CloakKey.rib, URLEncoder.encode(BuildConfig.VERSION_NAME, "UTF-8"))
//            put(CloakKey.crib,"")
//            put(CloakKey.stab,"")
//            put(CloakKey.gondola,"")
        }
    }

}