package com.tqs.filemanager.vm.activity

import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.vm.base.BaseVM

class SplashVM: BaseVM() {
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "Splash"
        }

    var progressValue = MutableLiveData<Int>(0)
}