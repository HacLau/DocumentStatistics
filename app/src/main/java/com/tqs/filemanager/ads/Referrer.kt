package com.tqs.filemanager.ads

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerClient.InstallReferrerResponse
import com.android.installreferrer.api.InstallReferrerStateListener
import com.tqs.filemanager.vm.utils.RepositoryUtils
import com.tqs.filemanager.vm.utils.logE

val buyUserList: Array<String> = arrayOf("fb4a", "gclid", "not%20set", "youtubeads", "%7b%22", "bytedance")
val fbUserList: Array<String> = arrayOf("fb4a")
var installReferrer = ""

fun isAdvertisingShouldShow(): Boolean {
    return when (referrerControl) {
        0 -> true
        1 -> false
        2 -> isBuyUser(installReferrer)
        else -> isFbUser(installReferrer)
    }
}

fun isFbUser(installReferrer: String): Boolean {
    return fbUserList.contains(installReferrer)
}

fun isBuyUser(installReferrer: String): Boolean {
    return buyUserList.contains(installReferrer)
}

fun initReferrer(context: Context) {
    installReferrer = RepositoryUtils.installReferrer.toString()
    if (installReferrer.isNotBlank()) {
        return
    }
    val referrerClient = InstallReferrerClient.newBuilder(context).build()
    referrerClient.startConnection(object : InstallReferrerStateListener {
        override fun onInstallReferrerSetupFinished(responseCode: Int) {
            kotlin.runCatching {
                when (responseCode) {
                    InstallReferrerResponse.OK -> {
                        referrerClient.installReferrer?.let {
                            installReferrer = it.installReferrer
                            RepositoryUtils.installReferrer = installReferrer
                        }
                    }

                    InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                    }

                    InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                    }

                }
                referrerClient.endConnection()
            }

        }

        override fun onInstallReferrerServiceDisconnected() {
            "onInstallReferrerServiceDisconnected".logE()
        }

    })
}
