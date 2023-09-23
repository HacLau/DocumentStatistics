package com.tqs.filecommander.net

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.tba.EventAdvertising
import com.tqs.filecommander.tba.EventCelsius
import com.tqs.filecommander.tba.EventCommon
import com.tqs.filecommander.tba.EventFiligree
import com.tqs.filecommander.tba.EventInstall
import com.tqs.filecommander.tba.EventQuixotic
import com.tqs.filecommander.vm.utils.application
import com.tqs.filecommander.vm.utils.logE
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import java.net.URLEncoder
import java.util.Locale
import java.util.UUID
import java.util.concurrent.TimeUnit


const val DEFAULT_CONNECT_TIME = 10L
const val DEFAULT_WRITE_TIME = 30L
const val DEFAULT_READ_TIME = 30L

object HttpHelper {
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(DEFAULT_CONNECT_TIME, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_WRITE_TIME, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_READ_TIME, TimeUnit.SECONDS)
        .build()
    private val builder: Request.Builder = Request.Builder().url(BuildConfig.TBAUrl)

    fun sendRequest(jsonObject: JSONObject, result: (String) -> Unit) {
        "requestJson = $jsonObject".logE()
        kotlin.runCatching {
            builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString())).build()
            val resp: Response = okHttpClient.newCall(builder.build()).execute()
            if (resp.isSuccessful) {
                val response: String = resp.body().string()
                response.logE()
                result.invoke(response)
            }
        }
    }

}


fun getRequestJson(key: String, jsonObject: JSONObject): JSONObject {
    val requestJson = JSONObject()
    requestJson.put(EventCommon.celsius, getCelsius())
    requestJson.put(EventCommon.quixotic, getQuixotic())
    requestJson.put(EventCommon.filigree, getFiligree())
    requestJson.put(key, jsonObject)
    return requestJson
}


private fun getCelsius(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(EventCelsius.ontogeny, Locale.getDefault().language)
    jsonObject.put(EventCelsius.animist, "twigging")
    jsonObject.put(EventCelsius.rib, BuildConfig.VERSION_NAME)
//        jsonObject.put(EventCelsius.crib, URLEncoder.encode("", "UTF-8"))
    jsonObject.put(EventCelsius.noodle, BuildConfig.APPLICATION_ID)
//        jsonObject.put(EventCelsius.previous, "")
    return jsonObject
}

@SuppressLint("HardwareIds")
private fun getQuixotic(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(EventQuixotic.abbey, URLEncoder.encode(Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID), "UTF-8"))
//        jsonObject.put(EventQuixotic.nepotism, "")
//        jsonObject.put(EventQuixotic.alasdair, "")
    jsonObject.put(EventQuixotic.sofia, UUID.randomUUID())
    jsonObject.put(EventQuixotic.fusty, URLEncoder.encode(System.currentTimeMillis().toString(), "UTF-8"))
    jsonObject.put(EventQuixotic.weight, Locale.getDefault().language)
//        jsonObject.put(EventQuixotic.stab, "")
    jsonObject.put(EventQuixotic.fairfax, (application.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).simOperator)
    jsonObject.put(EventQuixotic.ohio, Build.VERSION.RELEASE)
//        jsonObject.put(EventQuixotic.freeze, "")
    return jsonObject
}

private fun getFiligree(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(EventFiligree.invent, application.resources.displayMetrics.let { "${it.widthPixels}*${it.heightPixels}" })
    jsonObject.put(EventFiligree.nulls, URLEncoder.encode("", "UTF-8"))
//        jsonObject.put(EventFiligree.bell, "")
//        jsonObject.put(EventFiligree.forbear, URLEncoder.encode("","UTF-8"))
    jsonObject.put(EventFiligree.tabletop, "")
    jsonObject.put(EventFiligree.leaf, Build.MANUFACTURER)
    jsonObject.put(EventFiligree.benefit, UUID.randomUUID())
    jsonObject.put(EventFiligree.stubby, Build.MODEL)
    return jsonObject
}

fun getEventInstall(): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(EventInstall.squawk, Build.VERSION.RELEASE)
    jsonObject.put(EventInstall.omnibus, MMKVHelper.installReferrer)
    jsonObject.put(EventInstall.booty, MMKVHelper.installReferrerVersion)
    jsonObject.put(EventInstall.surname, "")
    jsonObject.put(EventInstall.hades, "")
    jsonObject.put(EventInstall.cacao, MMKVHelper.referrerClickTimestampSeconds)
    jsonObject.put(EventInstall.dough, MMKVHelper.installBeginTimestampSeconds)
    jsonObject.put(EventInstall.chunky, MMKVHelper.referrerClickTimestampServerSeconds)
    jsonObject.put(EventInstall.Is, MMKVHelper.installBeginTimestampServerSeconds)
    jsonObject.put(EventInstall.hoy, application.packageManager.getPackageInfo(application.packageName, 0).firstInstallTime)
    jsonObject.put(EventInstall.fury, application.packageManager.getPackageInfo(application.packageName, 0).lastUpdateTime)
    jsonObject.put(EventInstall.smallish, MMKVHelper.googlePlayInstantParam)
    return jsonObject
}

fun getEventSession(): JSONObject {
    return JSONObject()
}

fun getEventAdvertising(
    adsType: String ,
    adsId: String,
    adsPlat: String = "admob",
    adsSDK: String,
    adsIndex: String
): JSONObject {
    val jsonObject = JSONObject()
//    jsonObject.put(EventAdvertising.blink, "")
//    jsonObject.put(EventAdvertising.sank, "")
    jsonObject.put(EventAdvertising.monetary, adsPlat)
    jsonObject.put(EventAdvertising.monomer, adsSDK)
    jsonObject.put(EventAdvertising.upland, adsId)
    jsonObject.put(EventAdvertising.sex, adsIndex)
//    jsonObject.put(EventAdvertising.exceed, "")
//    jsonObject.put(EventAdvertising.millikan, "")
    jsonObject.put(EventAdvertising.bolo, adsType)
//    jsonObject.put(EventAdvertising.pencil, "")
//    jsonObject.put(EventAdvertising.wholly, "")
//    jsonObject.put(EventAdvertising.abstract, "")
    return jsonObject
}













