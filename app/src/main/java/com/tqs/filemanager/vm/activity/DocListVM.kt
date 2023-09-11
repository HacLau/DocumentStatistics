package com.tqs.filemanager.vm.activity

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.activity.DocListActivity
import com.tqs.filemanager.vm.base.BaseVM
import com.tqs.filemanager.vm.utils.FileUtils

class DocListVM:BaseVM() {
    var dataList = MutableLiveData<ArrayList<FileEntity>>()
    fun getAudioList(context: Context) {
        dataList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context) {
        dataList.value = FileUtils.getImgList(context)
    }

    fun getDownloadList(context: Context) {
        dataList.value = FileUtils.getVideoList(context)
    }

    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "AudioListVM"
        }
}