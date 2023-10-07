package com.tqs.filecommander.tba

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.webkit.WebSettings
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.getAndroidId
import org.json.JSONObject
import java.net.URLEncoder
import java.util.Locale
import java.util.UUID

fun getRequestJson(function: () -> MutableMap<String, Any> = { mutableMapOf<String, Any>() }): JSONObject =
    JSONObject().apply {
        this.put(EventCommon.celsius, getCelsius())
        this.put(EventCommon.quixotic, getQuixotic())
        this.put(EventCommon.filigree, getFiligree())
        function.invoke().forEach { (key, value) ->
            this.put(key, value)
        }

    }


private fun getCelsius(): JSONObject = JSONObject().apply {
    this.put(EventCelsius.ontogeny, Locale.getDefault().language)
    this.put(EventCelsius.animist, "twigging")
    this.put(EventCelsius.rib, BuildConfig.VERSION_NAME)
//        this.put(EventCelsius.crib, "")
    this.put(EventCelsius.noodle, BuildConfig.APPLICATION_ID)
//        this.put(EventCelsius.previous, "")
}

@SuppressLint("HardwareIds")
private fun getQuixotic(): JSONObject = JSONObject().apply {
    this.put(EventQuixotic.abbey, getAndroidId())
//        this.put(EventQuixotic.nepotism, "")
//        this.put(EventQuixotic.alasdair, "")
    this.put(EventQuixotic.sofia, getAndroidId())
    this.put(EventQuixotic.fusty, System.currentTimeMillis().toString())
    this.put(EventQuixotic.weight, Locale.getDefault().language)
//        this.put(EventQuixotic.stab, "")
    this.put(EventQuixotic.fairfax, (application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simOperator)
    this.put(EventQuixotic.ohio, Build.VERSION.RELEASE)
//        this.put(EventQuixotic.freeze, "")
}

private fun getFiligree(): JSONObject = JSONObject().apply {
    this.put(EventFiligree.invent, application.resources.displayMetrics.let { "${it.widthPixels}*${it.heightPixels}" })
    this.put(EventFiligree.nulls, "")
//        this.put(EventFiligree.bell, "")
//        this.put(EventFiligree.forbear, "","UTF-8"))
    this.put(EventFiligree.tabletop, TBAHelper.getGAId())
    this.put(EventFiligree.leaf, Build.MANUFACTURER)
    this.put(EventFiligree.benefit, UUID.randomUUID().toString())
    this.put(EventFiligree.stubby, Build.MODEL)
}

fun getEventInstall(): JSONObject = JSONObject().apply {
    this.put(EventInstall.squawk, "build/${Build.ID}")
    this.put(EventInstall.omnibus, MMKVHelper.installReferrer)
    this.put(EventInstall.booty, MMKVHelper.installReferrerVersion)
    this.put(EventInstall.surname, WebSettings.getDefaultUserAgent(application))
    this.put(EventInstall.hades, if (TBAHelper.getGAIdLimit()) "enough" else "shrub")
    this.put(EventInstall.cacao, MMKVHelper.referrerClickTimestampSeconds)
    this.put(EventInstall.dough, MMKVHelper.installBeginTimestampSeconds)
    this.put(EventInstall.chunky, MMKVHelper.referrerClickTimestampServerSeconds)
    this.put(EventInstall.Is, MMKVHelper.installBeginTimestampServerSeconds)
    this.put(EventInstall.hoy, TBAHelper.firstInstall)
    this.put(EventInstall.fury, TBAHelper.lastUpdate)
    this.put(EventInstall.smallish, MMKVHelper.googlePlayInstantParam)
}

fun getEventSession(): JSONObject = JSONObject().apply { }

fun getEventAds(): JSONObject =
    JSONObject().apply {
        put("impasse", "simon")
    }


fun getEventAdvertising(
    adsType: String,
    adsId: String,
    adsPlat: String = "admob",
    adsSDK: String,
    adsIndex: String
): MutableMap<String, String> =
    mutableMapOf<String, String>().apply {
//    this.put(EventAdvertising.blink, "")
//    this.put(EventAdvertising.sank, "")
        this.put(EventAdvertising.monetary, adsPlat)
        this.put(EventAdvertising.monomer, adsSDK)
        this.put(EventAdvertising.upland, adsId)
        this.put(EventAdvertising.sex, adsIndex)
//    this.put(EventAdvertising.exceed, "")
//    this.put(EventAdvertising.millikan, "")
        this.put(EventAdvertising.bolo, adsType)
//    this.put(EventAdvertising.pencil, "")
//    this.put(EventAdvertising.wholly, "")
//    this.put(EventAdvertising.abstract, "")
    }

fun getEventPoints(map: MutableMap<String, Any?>): JSONObject = JSONObject().apply {
    map.forEach { (key, value) ->
        put(key, value)
    }
}
