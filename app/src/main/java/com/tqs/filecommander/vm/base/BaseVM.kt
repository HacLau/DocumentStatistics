package com.tqs.filecommander.vm.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

abstract class BaseVM : ViewModel() {
    abstract val title: LiveData<String>
}