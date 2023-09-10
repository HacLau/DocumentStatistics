package com.tqs.filemanager.ui.activity.image

import androidx.lifecycle.ViewModelProvider
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImagePreviewBinding
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.vm.activity.image.ImageListVM
import com.tqs.filemanager.vm.activity.image.ImagePreviewVM

class ImagePreviewActivity : BaseActivity<ActivityImagePreviewBinding, ImagePreviewVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_preview
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[ImagePreviewVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
    }
}