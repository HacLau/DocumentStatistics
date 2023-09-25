package com.tqs.filecommander.vm.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.vm.base.BaseVM
import com.tqs.filecommander.utils.FileUtils

class DocListVM : BaseVM() {
    var dataList = MutableLiveData<ArrayList<FileEntity>>()
    var listSelectCount = MutableLiveData<Int>(0)
    fun getImageList(context: Context) {
        dataList.value = FileUtils.getImgListOrderDescByDate(context)
    }
    fun getVideoList(context: Context) {
        dataList.value = FileUtils.getVideoListOrderDescByDate(context)
    }
    fun getAudioList(context: Context) {
        dataList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context) {
        dataList.value = FileUtils.getDocList(context)
    }

    fun getDownloadList(context: Context) {
        dataList.value = FileUtils.getDownloadList(context)
    }

    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "AudioListVM"
        }
}