package com.tqs.filecommander.ui.activity

import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityNot404Binding
import com.tqs.filecommander.ui.base.BaseActivity
import com.tqs.filecommander.vm.activity.Not404VM

class Not404Activity(
    override val layoutId: Int = R.layout.activity_not404,
    override val TAG: String = "Not404Activity"
) : BaseActivity<ActivityNot404Binding, Not404VM>() {
    override fun initData() {
        binding.notBack.setOnClickListener {
            finish()
        }
    }

}