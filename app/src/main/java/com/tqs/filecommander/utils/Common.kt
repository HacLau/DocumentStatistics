package com.tqs.filecommander.utils

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.tqs.filecommander.FileCommanderApp
import com.tqs.filecommander.mmkv.MMKVHelper
import java.util.UUID

lateinit var application: FileCommanderApp

object Common {
    const val EMAIL: String = ""
    const val PAGE_TYPE = "pageType"
    const val IMAGE_LIST = "imageList"
    const val VIDEO_LIST = "videoList"
    const val AUDIO_LIST = "audioList"
    const val DOCUMENTS_LIST = "documentsList"
    const val DOWNLOAD_LIST = "downloadList"

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.BODY_SENSORS,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.CAMERA,
        Manifest.permission.READ_CALENDAR,
        Manifest.permission.POST_NOTIFICATIONS
    )
    val pageArray = arrayOf(
        IMAGE_LIST,
        AUDIO_LIST,
        DOCUMENTS_LIST,
        VIDEO_LIST,
        DOWNLOAD_LIST
    )
}

@SuppressLint("HardwareIds")
fun getAndroidId(): String {
    return MMKVHelper.androidId ?: Settings.Secure.getString(application.contentResolver, Settings.Secure.ANDROID_ID)?.apply {
        MMKVHelper.androidId = this
    } ?: UUID.randomUUID().toString().apply {
        MMKVHelper.androidId = this
    }
}