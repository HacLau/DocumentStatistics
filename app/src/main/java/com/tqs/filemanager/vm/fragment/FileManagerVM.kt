package com.tqs.filemanager.vm.fragment

import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.base.BaseVM
import com.tqs.filemanager.vm.utils.FileUtils

class FileManagerVM : BaseVM() {
    override val title : LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "This is File Manager Fragment"
        }
    var totalSpace = MutableLiveData<Long>(0L)
    var availSpace = MutableLiveData<Long>(0L)
    var progressValue = MutableLiveData<Long>(0L)

    var imageList = MutableLiveData<ArrayList<FileEntity>>()
    var audioList = MutableLiveData<ArrayList<FileEntity>>()
    var videoList = MutableLiveData<ArrayList<FileEntity>>()
    var documentsList = MutableLiveData<ArrayList<FileEntity>>()
    var downloadList = MutableLiveData<ArrayList<FileEntity>>()

    var imageListSize = MutableLiveData<Long>(0L)
    var audioListSize = MutableLiveData<Long>(0L)
    var videoListSize = MutableLiveData<Long>(0L)
    var documentsListSize = MutableLiveData<Long>(0L)
    var downloadListSize = MutableLiveData<Long>(0L)

    private val memoryUnit:Int = 1000
    private val kBUnit : Int = memoryUnit
    private val mBUnit : Int = kBUnit * memoryUnit
    private val gBUnit : Int = mBUnit * memoryUnit
    fun getMemoryInfo() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val blockSize = sf.blockSizeLong
            val blockCount = sf.blockCountLong
            val availCount = sf.availableBlocksLong
            totalSpace.value = blockSize * blockCount / gBUnit
            availSpace.value = availCount * blockSize / gBUnit
            progressValue.value = availSpace.value
        }
    }

    fun getImageList(context: Context){
        imageList.value = FileUtils.getImgList(context)
    }

    fun getVideoList(context: Context){
        videoList.value = FileUtils.getVideoList(context)

    }

    fun getAudioList(context: Context){
        audioList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context){
        documentsList.value = FileUtils.getDocList(context)
    }

    fun getDownloadList(context: Context){
        downloadList.value = FileUtils.getDownloadList(context)
    }

    fun getImageListSize(){
        imageListSize.value = 0L
        for (fileEntity in imageList.value!!){
            imageListSize.value = imageListSize.value!! + fileEntity.size
        }
        imageListSize.value = imageListSize.value!! /  mBUnit
    }

    fun getVideoListSize(){
        videoListSize.value = 0L
        for (fileEntity in videoList.value!!){
            videoListSize.value = videoListSize.value!! + fileEntity.size
        }
        videoListSize.value = videoListSize.value!! /  mBUnit
    }

    fun getAudioListSize(){
        audioListSize.value = 0L
        for (fileEntity in audioList.value!!){
            audioListSize.value = audioListSize.value!! + fileEntity.size
        }
        audioListSize.value = audioListSize.value!! /  mBUnit
    }

    fun getDocumentsListSize(){
        documentsListSize.value = 0L
        for (fileEntity in documentsList.value!!){
            documentsListSize.value = documentsListSize.value!! + fileEntity.size
        }
        documentsListSize.value = documentsListSize.value!! /  mBUnit
    }

    fun getDownloadListSize(){
        downloadListSize.value = 0L
        for (fileEntity in downloadList.value!!){
            downloadListSize.value = downloadListSize.value!! + fileEntity.size
        }
        downloadListSize.value = downloadListSize.value!! /  mBUnit
    }


}