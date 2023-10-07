package com.tqs.filecommander.vm

import android.app.Activity
import android.app.ActivityManager
import android.app.ActivityManager.MemoryInfo
import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.base.BaseVM
import com.tqs.filecommander.utils.FileUtils
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

class FileCommanderVM : BaseVM() {
    override var title: LiveData<String> = MutableLiveData<String>().apply {
            value = "This is File Commander Fragment"
        }
    var totalSpace = MutableLiveData<String>()
    var availSpace = MutableLiveData<String>()
    var progressValue = MutableLiveData<Int>(0)
    var hadPermission = MutableLiveData<Boolean>(false)

    var imageList = MutableLiveData<ArrayList<FileEntity>>()
    var audioList = MutableLiveData<ArrayList<FileEntity>>()
    var videoList = MutableLiveData<ArrayList<FileEntity>>()
    var documentsList = MutableLiveData<ArrayList<FileEntity>>()
    var downloadList = MutableLiveData<ArrayList<FileEntity>>()

    var imageSpaceSize = MutableLiveData<String>()
    var audioSpaceSize = MutableLiveData<String>()
    var videoSpaceSize = MutableLiveData<String>()
    var documentsSpaceSize = MutableLiveData<String>()
    var downloadSpaceSize = MutableLiveData<String>()

    fun getMemoryInfo() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sf = StatFs(Environment.getExternalStorageDirectory().path)
            val blockSize = sf.blockSizeLong
            val blockCount = sf.blockCountLong
            val availCount = sf.availableBlocksLong
            totalSpace.value = FileUtils.getTwoDigitsSpace(blockSize * blockCount)
            availSpace.value = FileUtils.getTwoDigitsSpace((blockCount - availCount) * blockSize)
            progressValue.value = ((blockCount - availCount) * 1.0 / blockCount * 100).toInt()
        }
    }

    fun getImgListOrderDescByDate(context: Context) {
        imageList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    fun getVideoListOrderDescByDate(context: Context) {
        videoList.value = FileUtils.getVideoListOrderDescByDate(context)

    }

    fun getAudioList(context: Context) {
        audioList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context) {
        documentsList.value = FileUtils.getDocList(context)
    }

    fun getDownloadList(context: Context) {
        downloadList.value = FileUtils.getDownloadList(context)
    }

    fun getImageListSize() {
        var size = 0L
        for (fileEntity in imageList.value!!) {
            size += fileEntity.size
        }
        imageSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getVideoListSize() {
        var size = 0L
        for (fileEntity in videoList.value!!) {
            size += fileEntity.size
        }
        videoSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getAudioListSize() {
        var size = 0L
        for (fileEntity in audioList.value!!) {
            size += fileEntity.size
        }
        audioSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getDocumentsListSize() {
        var size = 0L
        for (fileEntity in documentsList.value!!) {
            size += fileEntity.size
        }
        documentsSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }

    fun getDownloadListSize() {
        var size = 0L
        for (fileEntity in downloadList.value!!) {
            size += fileEntity.size
        }
        downloadSpaceSize.value = FileUtils.getTwoDigitsSpace(size)
    }


}