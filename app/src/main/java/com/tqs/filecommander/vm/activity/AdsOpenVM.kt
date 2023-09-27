package com.tqs.filecommander.vm.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.base.BaseVM

class AdsOpenVM : BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "AudioListVM"
        }
}