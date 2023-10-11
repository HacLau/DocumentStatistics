package com.tqs.filecommander.cloak

import android.os.Build
import com.tqs.filecommander.BuildConfig
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.net.HttpHelper
import com.tqs.filecommander.tba.EventPoints
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.encode
import com.tqs.filecommander.utils.getAndroidId
import com.tqs.filecommander.utils.logD
import com.tqs.filecommander.utils.logE
import org.json.JSONObject

object CloakHelper {
    var cloakState = CloakKey.poem
    private var reloadTimes = 0
    fun getCloakConfig() {
        TBAHelper.updatePoints(EventPoints.filec_cloak_start)
        val startSecond = System.currentTimeMillis() / 1000
        HttpHelper.sendRequestGet(
            jsonObject = getCloakJsonConfig(),
            resultSuccess = {
                cloakState = if (it.isNullOrBlank()) {
                    CloakKey.poem
                } else {
                    it
                }

                MMKVHelper.cloakState = it
                reloadTimes = 0
                TBAHelper.updatePoints(
                    EventPoints.filec_cloak_get, mutableMapOf(
                        EventPoints.Time to System.currentTimeMillis() / 1000 - startSecond,
                        EventPoints.getsuccess to true,
                        EventPoints.source to cloakState
                    )
                )

            }, resultFailed = { code, message ->
                if (reloadTimes++ < 20)
                    getCloakConfig()
                TBAHelper.updatePoints(
                    EventPoints.filec_cloak_get, mutableMapOf(
                        EventPoints.Time to System.currentTimeMillis() / 1000 - startSecond,
                        EventPoints.getsuccess to false,
                        EventPoints.source to cloakState
                    )
                )
            })

    }

    private fun getCloakJsonConfig(): JSONObject {
        return JSONObject().apply {
            put(CloakKey.sofia, getAndroidId().encode())
            put(CloakKey.fusty, System.currentTimeMillis().toString().encode())
            put(CloakKey.stubby, Build.MODEL.encode())
//            put(CloakKey.noodle, BuildConfig.APPLICATION_ID.encode())
            put(CloakKey.noodle, "com.file.commander.accelerate".encode())
            put(CloakKey.ohio, Build.VERSION.RELEASE.encode())
//            put(CloakKey.previous,"")
            put(CloakKey.tabletop, MMKVHelper.GaId)

            put(CloakKey.abbey, getAndroidId().encode())
            put(CloakKey.animist, "twigging".encode())
//            put(CloakKey.bell, "")
            put(CloakKey.rib, BuildConfig.VERSION_NAME.encode())
//            put(CloakKey.crib,"")
//            put(CloakKey.stab,"")
//            put(CloakKey.gondola,"")
        }
    }

}