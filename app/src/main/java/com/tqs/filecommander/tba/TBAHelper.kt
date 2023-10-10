package com.tqs.filecommander.tba

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tqs.filecommander.net.HttpHelper
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

object TBAHelper {
    val firstInstall by lazy { application.packageManager.getPackageInfo(application.packageName, 0).firstInstallTime }
    val lastUpdate by lazy { application.packageManager.getPackageInfo(application.packageName, 0).lastUpdateTime }

    fun getAds(callback: (String, Boolean) ->Unit  ) {
        CoroutineScope(Dispatchers.IO + SupervisorJob()).launch {
            try {
                val info = AdvertisingIdClient.getAdvertisingIdInfo(application)
                callback.invoke(info.id?:"", info.isLimitAdTrackingEnabled)
            }catch (e:Exception) {
                callback.invoke("",false)
            }
        }
    }

    fun updateSession() {
        Thread{
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.session, getEventSession())
                    }
                }, resultSuccess = {
                    "updateSession response : $it".logE()
                }, resultFailed = { code, message ->
                    "updateSession response fail response code $code & message $message".logE()
                }
            )
        }.start()

    }

    fun updateInstall() {
        Thread{
            "updateInstall ".logE()
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.install, getEventInstall())
                    }
                },
                resultSuccess = {
                    "updateInstall response : $it".logE()
                }, resultFailed = { code, message ->
                    "updateInstall response fail response code $code & message $message".logE()

                })
        }.start()

    }

    fun updateAdvertising(
        adsType: String,
        adsId: String,
        adsPlat: String = "admob",
        adsSDK: String,
        adsIndex: String
    ) {
        Thread{
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.eventName, "simon")
                        putAll(getEventAdvertising(adsType, adsId, adsPlat, adsSDK, adsIndex))
                    }
                },
                resultSuccess = {
                    "updateAdvertising response : $it".logE()
                }, resultFailed = { code, message ->

                    "updateAdvertising response fail response code $code & message $message".logE()
                })
        }.start()

    }

    fun updatePoints(eventValue: String, map: MutableMap<String, Any?> = mutableMapOf(), userEvent:String = eventValue) {
        Thread{
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.eventName, eventValue)
                        put(userEvent, getEventPoints(map))
                    }
                },
                resultSuccess = {
                    "updatePoints response : $it".logE()
                }, resultFailed = { code, message ->

                    "updatePoints response fail response code $code & message $message".logE()
                })
        }.start()

    }
}