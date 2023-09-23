package com.tqs.filecommander.vm.utils

import android.Manifest
import com.tqs.filecommander.FileCommanderApp

lateinit var application:FileCommanderApp
object Common {
    const val EMAIL: String = ""
    const val PAGE_TYPE = "pageType"
    const val IMAGE_LIST = "imageList"
    const val VIDEO_LIST = "videoList"
    const val AUDIO_LIST = "audioList"
    const val DOCUMENTS_LIST = "documentsList"
    const val DOWNLOAD_LIST = "downloadList"

    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

}