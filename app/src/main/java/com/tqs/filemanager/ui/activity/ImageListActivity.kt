package com.tqs.filemanager.ui.activity

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImageListBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.ImageVideoListAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.MediaItemDecoration
import com.tqs.filemanager.vm.activity.ImageListVM
import com.tqs.filemanager.vm.utils.Common

class ImageListActivity : BaseActivity<ActivityImageListBinding, ImageListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName
    private var showImageList: ArrayList<FileEntity> = arrayListOf()
    private lateinit var mImageAdapter:ImageVideoListAdapter
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[ImageListVM::class.java]
        binding.titleBar.setLeftClickListener {
            finish()
        }
        when(intent.getStringExtra(Common.PAGE_TYPE)){
            Common.IMAGE_LIST ->{
                viewModel.getImageList(this)
                binding.titleBar.setTitleText("Image")
            }
            Common.VIDEO_LIST ->{
                viewModel.getVideoList(this)
                binding.titleBar.setTitleText("Video")
            }
        }
        getImageViewShowList()
        setImageListAdapter()
    }

    private fun getImageViewShowList() {
        showImageList = viewModel.changeShowImageList(viewModel.imageList.value)
    }

    private fun setImageListAdapter() {

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
        mImageAdapter = ImageVideoListAdapter(this, showImageList)
        mImageAdapter.setOnItemClickListener(object : ImageVideoListAdapter.OnItemClickListener {
            override fun onItemClick(position: Int, touchView: String) {
                if (mImageAdapter.touchState == mImageAdapter.LONGSTATE){
                    if (showImageList[position].selected){
                        viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                    }else{
                        viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                    }
                    showImageList[position].selected = !showImageList[position].selected
                    mImageAdapter.setData(showImageList)
                    mImageAdapter.notifyItemChanged(position)
                }else {
                    when (touchView) {
                        mImageAdapter.TOUCHIMAGEVIEW -> {
                            toPreviewImage(position)
                        }
                        mImageAdapter.TOUCHPLAYVIEW -> {
                            toPreviewImage(position)
                        }
                    }
                }
                Log.e(TAG, "click $touchView and list data ${showImageList[position].name}")
            }
        })

        mImageAdapter.setOnItemLongClickListener(object :
            ImageVideoListAdapter.OnItemLongClickListener {
            override fun onItemLongClick(position: Int) {
                Log.e(TAG, "long click $position and list data ${showImageList[position].name}")
                // show select all and radio
                showImageList[position].selected = !showImageList[position].selected
                mImageAdapter.touchState = mImageAdapter.LONGSTATE
                mImageAdapter.setData(showImageList)
                mImageAdapter.notifyDataSetChanged()
            }

        })
        binding.rvImageList.adapter = mImageAdapter

    }

    private fun toPreviewImage(position: Int) {
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra("selectImageIndex", position - viewModel.getEmptyData(position,showImageList))
        intent.putExtra("previewFileList", Gson().toJson(viewModel.imageList.value))
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (viewModel.listSelectCount.value!! > 0){
            viewModel.listSelectCount.value = 0
            for (file in viewModel.imageList.value!!){
                if (TextUtils.isEmpty(file.dateString))
                    file.selected = false
            }
            mImageAdapter.touchState = mImageAdapter.CLICKSTATE
            mImageAdapter.setData(showImageList)
            mImageAdapter.notifyDataSetChanged()
        }else{
            super.onBackPressed()
        }
    }
}