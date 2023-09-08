package com.tqs.filemanager.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tqs.filemanager.vm.base.BaseVM

class FileManagerVM : BaseVM() {
    override val title : LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "This is File Manager Fragment"
        }
    var progressValue = MutableLiveData<Long>(0L)
}