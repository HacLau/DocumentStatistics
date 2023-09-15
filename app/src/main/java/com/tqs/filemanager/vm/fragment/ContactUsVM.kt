package com.tqs.filemanager.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class ContactUsVM : BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "This is Contact Us Fragment"
        }
}