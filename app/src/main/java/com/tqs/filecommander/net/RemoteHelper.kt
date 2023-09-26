package com.tqs.filecommander.net

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.tqs.filecommander.cloak.CloakHelper
import com.tqs.filecommander.notification.NotificationEntity
import com.tqs.filecommander.notification.NotificationHelper
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
        kotlin.runCatching {
            val json: String = remoteConfig.get("fcpop").asString()
            if (json.isBlank())
                return
            val notificationEntity = Gson().fromJson(json, NotificationEntity::class.java)
            ReferrerHelper.setReferrerControl(notificationEntity.referrerSwitch)
            NotificationHelper.setNotification(notificationEntity)
        }
    }

    private fun getAdvertisingConfig() {
        kotlin.runCatching {
            val json: String = remoteConfig.get("fc_ad_config").asString()
            if (json.isBlank())
                return
        }
    }

}