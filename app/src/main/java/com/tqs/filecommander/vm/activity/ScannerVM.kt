package com.tqs.filecommander.vm.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.vm.base.BaseVM

class ScannerVM:BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>("Scanner")
}