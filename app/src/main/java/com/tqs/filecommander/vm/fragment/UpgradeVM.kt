package com.tqs.filecommander.vm.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.base.BaseVM

class UpgradeVM : BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "This is Upgrade Fragment"
        }
}