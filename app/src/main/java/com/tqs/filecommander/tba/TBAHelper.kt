package com.tqs.filecommander.tba

import com.tqs.filecommander.net.*
import com.tqs.filecommander.utils.logE

object TBAHelper {
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