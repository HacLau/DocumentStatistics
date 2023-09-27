package com.tqs.filecommander.referrer

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.InstallReferrerStateListener
import com.tqs.filecommander.mmkv.MMKVHelper
import com.tqs.filecommander.tba.TBAHelper
import com.tqs.filecommander.utils.logE

object ReferrerHelper {
    private val buyUserList: Array<String> = arrayOf("fb4a", "gclid", "not%20set", "youtubeads", "%7b%22", "bytedance")
    private val fbUserList: Array<String> = arrayOf("fb4a")
    var installReferrer = ""
    private var referrerControl = 1
    fun setReferrerControl(control: Int) {
        this.referrerControl = control
    }

    fun isAdvertisingShouldShow(): Boolean {
        return when (referrerControl) {
            0 -> true
            1 -> false
            2 -> isBuyUser(installReferrer)
            else -> isFbUser(installReferrer)
        }
    }

    private fun isFbUser(installReferrer: String): Boolean {
        return fbUserList.contains(installReferrer)
    }

    fun isBuyUser(installReferrer: String): Boolean {
        return buyUserList.contains(installReferrer)
    }

    fun initReferrer(context: Context) {
        installReferrer = MMKVHelper.installReferrer.toString()
        if (installReferrer.isNotBlank()) {
            return
        }
        runCatching {
            val referrerClient = InstallReferrerClient.newBuilder(context).build()
            referrerClient.startConnection(object : InstallReferrerStateListener {
                override fun onInstallReferrerSetupFinished(responseCode: Int) {
                    runCatching {
                        when (responseCode) {
                            InstallReferrerResponse.OK -> {
                                referrerClient.installReferrer?.let {

                                    installReferrer = it.installReferrer
                                    MMKVHelper.installReferrer = installReferrer
                                    MMKVHelper.referrerClickTimestampSeconds = it.referrerClickTimestampSeconds
                                    MMKVHelper.referrerClickTimestampServerSeconds = it.referrerClickTimestampServerSeconds
                                    MMKVHelper.installBeginTimestampSeconds = it.installBeginTimestampSeconds
                                    MMKVHelper.installBeginTimestampServerSeconds = it.installBeginTimestampServerSeconds
                                    MMKVHelper.googlePlayInstantParam = it.googlePlayInstantParam

                                    MMKVHelper.installReferrer = it.installVersion
                                    // to update tba data of install
                                    if (MMKVHelper.firstLaunchApp) {
                                        MMKVHelper.firstLaunchApp = false
                                        TBAHelper.updateInstall()
                                    }
                                }
                            }

                            InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                                //Local Code: Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI
                            }

                            InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                            }

                        }
                        referrerClient.endConnection()
                    }

                }

                override fun onInstallReferrerServiceDisconnected() {
                    "Referrer onInstallReferrerServiceDisconnected".logE()
                }

            })
        }
    }
}