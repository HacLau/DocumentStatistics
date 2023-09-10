package com.tqs.filemanager.vm.fragment

import android.content.Context
import android.os.Environment
import android.os.StatFs
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.base.BaseVM
import com.tqs.filemanager.vm.utils.FileManagerUtil
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    private val memoryUnit : Int = 1000 * 1000 * 1000
    private val spaceUnit : Int = 1000 * 1000
    fun getMemoryInfo() {
        val state = Environment.getExternalStorageState()
        if (Environment.MEDIA_MOUNTED == state) {
            val sdcardDir = Environment.getExternalStorageDirectory()
            val sf = StatFs(sdcardDir.path)
            val blockSize = sf.blockSizeLong
            val blockCount = sf.blockCountLong
            val availCount = sf.availableBlocksLong
            totalSpace.value = blockSize * blockCount / memoryUnit
            availSpace.value = availCount * blockSize / memoryUnit
            progressValue.value = availSpace.value
        }
    }

    fun getImageList(context: Context){
        imageList.value = FileManagerUtil.getImgList(context)
    }

    fun getVideoList(context: Context){
        videoList.value = FileManagerUtil.getVideoList(context)

    }

    fun getAudioList(context: Context){
        audioList.value = FileManagerUtil.getAudioList(context)
    }

    fun getDocumentsList(context: Context){
        documentsList.value = FileManagerUtil.getDocList(context)
    }

    fun getDownloadList(context: Context){
        downloadList.value = FileManagerUtil.getDownloadList(context)
    }

    fun getImageListSize(){
        imageListSize.value = 0L
        for (fileEntity in imageList.value!!){
            imageListSize.value = imageListSize.value!! + fileEntity.size
        }
        imageListSize.value = imageListSize.value!! /  spaceUnit
    }

    fun getVideoListSize(){
        videoListSize.value = 0L
        for (fileEntity in videoList.value!!){
            videoListSize.value = videoListSize.value!! + fileEntity.size
        }
        videoListSize.value = videoListSize.value!! /  spaceUnit
    }

    fun getAudioListSize(){
        audioListSize.value = 0L
        for (fileEntity in audioList.value!!){
            audioListSize.value = audioListSize.value!! + fileEntity.size
        }
        audioListSize.value = audioListSize.value!! /  spaceUnit
    }

    fun getDocumentsListSize(){
        documentsListSize.value = 0L
        for (fileEntity in documentsList.value!!){
            documentsListSize.value = documentsListSize.value!! + fileEntity.size
        }
        documentsListSize.value = documentsListSize.value!! /  spaceUnit
    }

    fun getDownloadListSize(){
        downloadListSize.value = 0L
        for (fileEntity in downloadList.value!!){
            downloadListSize.value = downloadListSize.value!! + fileEntity.size
        }
        downloadListSize.value = downloadListSize.value!! /  spaceUnit
    }


}