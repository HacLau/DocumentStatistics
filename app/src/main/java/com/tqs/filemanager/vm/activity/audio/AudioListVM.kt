package com.tqs.filemanager.vm.activity.audio

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class AudioListVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "AudioListVM"
        }
}