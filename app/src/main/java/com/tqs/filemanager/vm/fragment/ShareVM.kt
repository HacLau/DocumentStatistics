package com.tqs.filemanager.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tqs.filemanager.vm.base.BaseVM

class ShareVM  : BaseVM() {
    override val title : LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "This is Share Fragment"
        }
}