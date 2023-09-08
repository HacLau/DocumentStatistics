package com.tqs.filemanager.vm.activity.video

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class VideoListVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "VideoListVM"
        }
}