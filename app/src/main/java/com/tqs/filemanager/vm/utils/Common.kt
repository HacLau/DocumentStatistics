package com.tqs.filemanager.vm.utils

import android.Manifest

object Common {
    const val EMAIL: String = ""
    const val PAGE_TYPE = "pageType"
    const val IMAGE_LIST = "imageList"
    const val VIDEO_LIST = "videoList"
    const val AUDIO_LIST = "audioList"
    const val DOCUMENTS_LIST = "documentsList"
    const val DOWNLOAD_LIST = "downloadList"

    const val EXTERNAL_STORAGE_PERMISSION = "EXTERNAL_STORAGE_PERMISSION"
    const val REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = "REQUEST_CODE_MANAGE_EXTERNAL_STORAGE"

    val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET
    )

}