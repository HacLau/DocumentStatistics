package com.tqs.filemanager.vm.activity.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class DownloadListVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "DownloadListVM"
        }
}