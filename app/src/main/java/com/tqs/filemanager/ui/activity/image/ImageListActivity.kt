package com.tqs.filemanager.ui.activity.image

import android.text.TextUtils
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImageListBinding
import com.tqs.filemanager.ui.adapter.ImageListAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.MediaItemDecoration
import com.tqs.filemanager.vm.activity.image.ImageListVM

class ImageListActivity : BaseActivity<ActivityImageListBinding, ImageListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[ImageListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        viewModel.getImageList(this)
        setImageListAdapter()
    }

    private fun setImageListAdapter() {
        val showImageList = viewModel.changeShowImageList(viewModel.imageList.value)
        val manager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (TextUtils.isEmpty(showImageList[position].dateString)) {
                    1
                } else {
                    3
                }
            }
        }
        binding.rvImageList.layoutManager = manager
        binding.rvImageList.addItemDecoration(MediaItemDecoration(6))
        binding.rvImageList.adapter = ImageListAdapter(this, showImageList)
    }
}