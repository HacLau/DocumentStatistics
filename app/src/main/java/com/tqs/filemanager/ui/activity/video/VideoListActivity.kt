package com.tqs.filemanager.ui.activity.video

import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityVideoListBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.ImageListAdapter
import com.tqs.filemanager.ui.adapter.VideoListAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.MediaItemDecoration
import com.tqs.filemanager.vm.activity.image.ImageListVM
import com.tqs.filemanager.vm.activity.video.VideoListVM

class VideoListActivity : BaseActivity<ActivityVideoListBinding, VideoListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_video_list
    override val TAG: String
        get() = this.packageName

    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[VideoListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        viewModel.getImageList(this)
        setVideoListAdapter()
        setOnClickListener()
    }

    private fun setVideoListAdapter() {
        val showImageList = viewModel.changeShowImageList(viewModel.videoList.value)
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
        binding.rvVideoList.layoutManager = manager
        binding.rvVideoList.addItemDecoration(MediaItemDecoration(6))
        binding.rvVideoList.adapter = VideoListAdapter(this, showImageList)
    }

    private fun setOnClickListener() {

    }
}