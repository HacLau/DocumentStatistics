package com.tqs.filemanager.vm.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class MainVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "Main"
        }
}