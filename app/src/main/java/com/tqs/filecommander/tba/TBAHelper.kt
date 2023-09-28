package com.tqs.filecommander.tba

import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.tqs.filecommander.net.*
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

object TBAHelper {
    val firstInstall by lazy { application.packageManager.getPackageInfo(application.packageName, 0).firstInstallTime }
    val lastUpdate by lazy { application.packageManager.getPackageInfo(application.packageName, 0).lastUpdateTime }
    private val advertisingInfo by lazy { AdvertisingIdClient.getAdvertisingIdInfo(application) }
    fun getGAId(): String {
        return advertisingInfo.id ?: ""
    }

    fun getGAIdLimit(): Boolean {
        return advertisingInfo.isLimitAdTrackingEnabled
    }

    fun updateSession() {
        "updateSession".logE()
        HttpHelper.sendRequestPost(
            jsonObject = getRequestJson(
                EventCommon.session,
                getEventSession()
            ), resultSuccess = {
                "updateSession $it".logE()
            }, resultFailed = { code, message ->

            }
        )
    }

    fun updateInstall() {
        "updateInstall".logE()
        HttpHelper.sendRequestPost(
            jsonObject = getRequestJson(
                EventCommon.install,
                getEventInstall()
            ),
            resultSuccess = {
                "updateInstall $it".logE()
            }, resultFailed = { code, message ->

            })
    }

    fun updateAdvertising(
        adsType: String,
        adsId: String,
        adsPlat: String = "admob",
        adsSDK: String,
        adsIndex: String
    ) {
        "updateAdvertising".logE()
        HttpHelper.sendRequestPost(
            jsonObject = getRequestJson(
                EventCommon.advertising,
                getEventAdvertising(adsType, adsId, adsPlat, adsSDK, adsIndex)
            ),
            resultSuccess = {
                "updateAdvertising $it".logE()
            }, resultFailed = { code, message ->

            })
    }
}