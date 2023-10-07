package com.tqs.filecommander.tba

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tqs.filecommander.net.*
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

object TBAHelper {
    val firstInstall by lazy { application.packageManager.getPackageInfo(application.packageName, 0).firstInstallTime }
    val lastUpdate by lazy { application.packageManager.getPackageInfo(application.packageName, 0).lastUpdateTime }
    private val advertisingInfo by lazy {
        AdvertisingIdClient.getAdvertisingIdInfo(application)
    }

    fun getGAId(): String {
        return advertisingInfo.id ?: ""
    }

    fun getGAIdLimit(): Boolean {
        return advertisingInfo.isLimitAdTrackingEnabled
    }

    fun updateSession() {
        "updateSession".logE()
        HttpHelper.sendRequestPost(
            jsonObject = getRequestJson {
                mutableMapOf<String, Any>().apply {
                    put(EventCommon.session, getEventSession())
                }
            }, resultSuccess = {
                "updateSession $it".logE()
            }, resultFailed = { code, message ->

            }
        )
    }

    fun updateInstall() {
        "updateInstall".logE()
        Thread {
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.install, getEventInstall())
                    }
                },
                resultSuccess = {
                    "updateInstall $it".logE()
                }, resultFailed = { code, message ->

                })
        }
    }

    fun updateAdvertising(
        adsType: String,
        adsId: String,
        adsPlat: String = "admob",
        adsSDK: String,
        adsIndex: String
    ) {
        "updateAdvertising".logE()
        Thread {
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.eventName, getEventAds())
                        putAll(getEventAdvertising(adsType, adsId, adsPlat, adsSDK, adsIndex))
                    }
                },
                resultSuccess = {
                    "updateAdvertising $it".logE()
                }, resultFailed = { code, message ->

                })
        }
    }

    fun updatePoints(eventValue: String, map: MutableMap<String, Any?> = mutableMapOf()) {
        "updatePoints".logE()
        Thread{
            HttpHelper.sendRequestPost(
                jsonObject = getRequestJson {
                    mutableMapOf<String, Any>().apply {
                        put(EventCommon.eventName, eventValue)
                        put(eventValue, getEventPoints(map))
                    }
                },
                resultSuccess = {
                    "updatePoints $it".logE()
                }, resultFailed = { code, message ->

                })
        }

    }
}