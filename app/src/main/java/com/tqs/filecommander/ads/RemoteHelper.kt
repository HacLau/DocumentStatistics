package com.tqs.filecommander.ads

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import org.json.JSONObject

var referrerControl = 1
class RemoteHelper {
    private val remoteConfig by lazy {
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
        }
    }

    private fun getUserType(){
        kotlin.runCatching {
            val json:String = remoteConfig.get("fcpop").asString()
            if (json.isBlank())
                return
            val jsonObject = JSONObject(json)
            referrerControl = jsonObject.optInt("fc_reffer",2)
        }
    }
}