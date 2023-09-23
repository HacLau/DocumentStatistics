package com.tqs.filecommander.ui.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel

abstract class BaseFragment<VB : ViewDataBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewModel: VM
    abstract val layoutId: Int
    public var REQUEST_CODE_PERMISSION = 0x00099

    abstract fun initData()
}