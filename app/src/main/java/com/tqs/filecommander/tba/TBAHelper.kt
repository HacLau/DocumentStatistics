package com.tqs.filecommander.tba

import com.tqs.filecommander.net.*
import com.tqs.filecommander.vm.utils.logE

object TBAHelper {
    fun updateSession() {
        "updateSession".logE()
        HttpHelper.sendRequest(
            getRequestJson(
                EventCommon.session,
                getEventSession()
            )
        ) {
            "updateSession $it".logE()
        }
    }

    fun updateInstall() {
        "updateInstall".logE()
        HttpHelper.sendRequest(
            getRequestJson(
                EventCommon.install,
                getEventInstall()
            )
        ) {
            "updateInstall $it".logE()
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
        HttpHelper.sendRequest(
            getRequestJson(
                EventCommon.advertising,
                getEventAdvertising(adsType, adsId, adsPlat, adsSDK, adsIndex)
            )
        ) {
            "updateAdvertising $it".logE()
        }
    }
}