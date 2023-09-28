package com.tqs.filecommander.ui.activity

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tqs.filecommander.R
import com.tqs.filecommander.databinding.ActivityImageListBinding
import com.tqs.filecommander.ads.AdsManager
import com.tqs.filecommander.adapter.ImageVideoListAdapter
import com.tqs.filecommander.base.BaseActivity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.ui.view.MediaItemDecoration
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.FileUtils
import com.tqs.filecommander.vm.MainVM

class ImageListActivity : BaseActivity<ActivityImageListBinding, MainVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName

    private var registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            viewModel.deletedFile = it.data?.getBooleanExtra("deleteResult", false) == true
            if (viewModel.deletedFile) {
                orderShowImageList()
            }
            setResult()
        }
    }

    private fun setResult() {
        if (viewModel.deletedFile) {
            setResult(RESULT_OK, Intent().apply {
                putExtra("deleteResult", true)
            })
        } else {
            setResult(RESULT_CANCELED, Intent().apply {
            })
        }

    }

    private var mDeleteDialog: ConfirmAndCancelDialog? = null
    override fun initData() {
        setStatusBarTransparent(this)
        setStatusBarLightMode(this, true)
        viewModel = ViewModelProvider(this)[MainVM::class.java]
        binding.titleBar.setLeftClickListener {
            setResult()
            finish()
        }
        binding.titleBar.setOrderVisible(true)
        binding.titleBar.setOrderClickListener {
            viewModel.currentOrder.value = when (viewModel.currentOrder.value) {
                viewModel.DESC -> viewModel.ASC
                viewModel.ASC -> viewModel.DESC
                else -> {
                    ""
                }
            }
            orderShowImageList()
        }
        binding.vImageDelete.visibility = View.GONE
        binding.titleBar.setSelectClickListener {
            when (viewModel.currentSelect.value) {
                viewModel.NONE -> {
                    selectAllShowImageList()
                    viewModel.currentSelect.value = viewModel.ALL
                    binding.titleBar.setSelectText("unselect")
                }

                viewModel.ALL -> {
                    unselectAllShowImageList()
                    viewModel.currentSelect.value = viewModel.NONE
                    binding.titleBar.setSelectText("select all")
                }
            }
        }
        viewModel.mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        orderShowImageList()
        viewModel.listSelectCount.observe(this) {
            if (it > 0) {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_selected)
            } else {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_normal)
            }
        }
        viewModel.dataList.observe(this) {
            getImageViewShowList()
        }
        binding.vImageDelete.setOnClickListener {
            AdsManager.adsInsertResultClean.showFullScreenAds(this@ImageListActivity) {
                showDeleteDialog()
            }
        }
        setImageListAdapter()
    }

    private fun showDeleteDialog() {
        if (mDeleteDialog == null) {
            mDeleteDialog = ConfirmAndCancelDialog(this, {
                mDeleteDialog?.dismiss()
            }, {
                mDeleteDialog?.dismiss()
                deleteSelectedImage()
                orderShowImageList()
            })

        }
        mDeleteDialog?.show()
    }

    private fun deleteSelectedImage() {
        for (file in viewModel.showDataList) {
            if (file.selected) {
                file.path?.let { FileUtils.deleteFile(it) }
            }
        }
        viewModel.mImageAdapter.touchState = viewModel.mImageAdapter.CLICKSTATE
        viewModel.listSelectCount.value = 0
        binding.titleBar.setOrderVisible(true)
        binding.vImageDelete.visibility = View.GONE
        setResult(RESULT_OK)
    }

    private fun unselectAllShowImageList() {
        viewModel.listSelectCount.value = 0
        for (file in viewModel.showDataList) {
            file.selected = false
        }
        viewModel.mImageAdapter.setData(viewModel.showDataList)
        viewModel.mImageAdapter.notifyDataSetChanged()
    }

    private fun selectAllShowImageList() {
        viewModel.listSelectCount.value = 0
        for (file in viewModel.showDataList) {
            if (!file.isTitle) {
                file.selected = true
                viewModel.listSelectCount.value = viewModel.listSelectCount.value!! + 1
            }
        }
        viewModel.mImageAdapter.setData(viewModel.showDataList)
        viewModel.mImageAdapter.notifyDataSetChanged()
    }

    private fun orderShowImageList() {
        viewModel.dataList.value = null
        when (viewModel.currentOrder.value) {
            viewModel.DESC -> {
                when (viewModel.mPageType) {
                    Common.IMAGE_LIST -> {
                        viewModel.getImageListOrderDescByDate(this)
                        binding.titleBar.setTitleText("Image")
                    }

                    Common.VIDEO_LIST -> {
                        viewModel.getVideoListOrderDescByDate(this)
                        binding.titleBar.setTitleText("Video")
                    }
                }
            }

            viewModel.ASC -> {
                when (viewModel.mPageType) {
                    Common.IMAGE_LIST -> {
                        viewModel.getImageListOrderAscByDate(this)
                        binding.titleBar.setTitleText("Image")
                    }

                    Common.VIDEO_LIST -> {
                        viewModel.getVideoListOrderAscByDate(this)
                        binding.titleBar.setTitleText("Video")
                    }
                }
            }
        }
    }

    private fun getImageViewShowList() {
        viewModel.showDataList = viewModel.changeShowImageList(viewModel.dataList.value)
        viewModel.mImageAdapter.setData(viewModel.showDataList)
        viewModel.mImageAdapter.notifyDataSetChanged()
    }

    private fun setImageListAdapter() {

        val manager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (viewModel.showDataList[position].isTitle) {
                    3
                } else {
                    1
                }
            }
        }
        binding.rvImageList.layoutManager = manager
        binding.rvImageList.addItemDecoration(MediaItemDecoration(6))
        viewModel.mImageAdapter = ImageVideoListAdapter(this, viewModel.showDataList, { position, touchView ->
            if (viewModel.mImageAdapter.touchState == viewModel.mImageAdapter.LONGSTATE
                && touchView != viewModel.mImageAdapter.TOUCHTITLEVIEW
            ) {
                if (viewModel.showDataList[position].selected) {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                    viewModel.currentSelect.value = viewModel.NONE
                    binding.titleBar.setSelectText("select all")
                } else {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                    if (viewModel.listSelectCount.value == viewModel.dataList.value?.size) {
                        viewModel.currentSelect.value = viewModel.ALL
                        binding.titleBar.setSelectText("unselect")
                    }
                }
                viewModel.showDataList[position].selected = !viewModel.showDataList[position].selected
                viewModel.mImageAdapter.setData(viewModel.showDataList)
                viewModel.mImageAdapter.notifyItemChanged(position)
            } else {
                when (touchView) {
                    viewModel.mImageAdapter.TOUCHIMAGEVIEW -> {
                        toPreviewImage(position)
                    }

                    viewModel.mImageAdapter.TOUCHPLAYVIEW -> {
                        toPreviewImage(position)
                    }
                }
            }
        }, { position ->
            if (!viewModel.showDataList[position].selected && viewModel.mImageAdapter.touchState != viewModel.mImageAdapter.LONGSTATE) {
                viewModel.mImageAdapter.touchState = viewModel.mImageAdapter.LONGSTATE
                viewModel.showDataList[position].selected = !viewModel.showDataList[position].selected
                viewModel.mImageAdapter.setData(viewModel.showDataList)
                binding.vImageDelete.visibility = View.VISIBLE
                viewModel.listSelectCount.value = 1
                binding.titleBar.setSelectVisible(true)
                viewModel.mImageAdapter.notifyDataSetChanged()
            }
        })
        binding.rvImageList.adapter = viewModel.mImageAdapter

    }

    private fun toPreviewImage(position: Int) {
        registerForActivityResult.launch(Intent(this, PreviewActivity::class.java).apply {
            putExtra("selectImageIndex", position - viewModel.getEmptyData(position, viewModel.showDataList))
            putExtra("previewFileList", Gson().toJson(viewModel.dataList.value))
            putExtra(Common.PAGE_TYPE, viewModel.mPageType)
        })
    }

    override fun onBackPressed() {
        if (viewModel.listSelectCount.value!! > 0 || viewModel.mImageAdapter.touchState == viewModel.mImageAdapter.LONGSTATE) {
            viewModel.mImageAdapter.touchState = viewModel.mImageAdapter.CLICKSTATE
            unselectAllShowImageList()
            binding.titleBar.setOrderVisible(true)
            binding.vImageDelete.visibility = View.GONE
        } else {
            super.onBackPressed()
            setResult()
        }
    }
}