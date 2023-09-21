package com.tqs.filemanager.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityNot404Binding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.Not404VM

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