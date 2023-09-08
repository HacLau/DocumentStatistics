package com.tqs.filemanager.vm.activity.documents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class DocumentListVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "DocumentListVM"
        }
}