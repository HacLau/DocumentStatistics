package com.tqs.filemanager.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.databinding.FragmentUpgradeBinding
import com.tqs.filemanager.vm.fragment.UpgradeVM

abstract class BaseFragment<VB: ViewDataBinding,VM:ViewModel>: Fragment()  {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId:Int

    abstract fun initData()
}