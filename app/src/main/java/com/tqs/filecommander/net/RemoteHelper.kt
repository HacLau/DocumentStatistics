package com.tqs.filecommander.net

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.tqs.filecommander.ads.AdsEntity
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.ads.AdsUserCost
import com.tqs.filecommander.cloak.CloakHelper
import com.tqs.filecommander.notification.NotificationController
import com.tqs.filecommander.notification.NotificationEntity
import com.tqs.filecommander.referrer.ReferrerHelper


class RemoteHelper {
    private val remoteConfig by lazy {
        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(remoteConfigSettings {
                minimumFetchIntervalInSeconds = 3600
            })
        }
    }

    fun fetch() {
        getConfig()
        remoteConfig.fetchAndActivate().addOnSuccessListener {
            getConfig()
        }
    }

    private fun getConfig() {
        getReferrerAndNotificationConfig()
        getAdvertisingConfig()
        CloakHelper.getCloakConfig()
    }

    private fun getReferrerAndNotificationConfig() {
        runCatching {
            val json: String = remoteConfig["fcpop"].asString()
            if (json.isBlank()) return
            val notificationEntity = Gson().fromJson(json, NotificationEntity::class.java)
            ReferrerHelper.setReferrerControl(notificationEntity.referrerSwitch)
            NotificationController.setNotification(notificationEntity)
        }
    }

    private fun getAdvertisingConfig() {
        runCatching {
            val json: String = remoteConfig["fc_ad_config"].asString()
            if (json.isBlank()) return
            AdsManager.adsEntity = Gson().fromJson(json,AdsEntity::class.java)
        }
    }

    private fun getGoogleConfig(){
        runCatching {
            val json:String = remoteConfig["FileCommander_toppercent"].asString()
            if (json.isBlank()) return
            adsUserCost = Gson().fromJson(json,AdsUserCost::class.java)
        }
    }

}