package com.tqs.filemanager.ui.activity.image

import android.view.View
import android.view.View.OnClickListener
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImageListBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.image.ImageListVM

class ImageListActivity : BaseActivity<ActivityImageListBinding, ImageListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        binding.titleBar.setLeftClickListener {
            finish()
        }
    }
}