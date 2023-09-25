package com.tqs.filecommander.ads

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.tqs.filecommander.utils.application

private val mFirebaseAnalytics = FirebaseAnalytics.getInstance(application)
//Analytics and update user's behavior
fun logEvent(eventName:String){
    mFirebaseAnalytics.logEvent(eventName,null)
}
fun logEvent(eventName: String,paramMap:MutableMap<String,Any?>){
    runCatching {
        mFirebaseAnalytics.logEvent(eventName, Bundle().apply {
            for (entry in paramMap.entries) {
                when (val value = entry.value) {
                    is Int -> putInt(entry.key, value)
                    is String -> putString(entry.key, value)
                    is Double -> putDouble(entry.key, value)
                    is Long -> putLong(entry.key, value)
                    else -> Unit
                }
            }
        })
    }
}
