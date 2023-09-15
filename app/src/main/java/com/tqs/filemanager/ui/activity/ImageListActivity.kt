package com.tqs.filemanager.ui.activity

import android.content.Intent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.ActivityImageListBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.ui.adapter.ImageVideoListAdapter
import com.tqs.filemanager.ui.base.BaseActivity
import com.tqs.filemanager.ui.view.ConfirmAndCancelDialog
import com.tqs.filemanager.ui.view.MediaItemDecoration
import com.tqs.filemanager.vm.activity.ImageListVM
import com.tqs.filemanager.vm.utils.Common
import com.tqs.filemanager.vm.utils.FileUtils

class ImageListActivity : BaseActivity<ActivityImageListBinding, ImageListVM>() {
    override val layoutId: Int
        get() = R.layout.activity_image_list
    override val TAG: String
        get() = this.packageName
    private var showImageList: ArrayList<FileEntity> = arrayListOf()
    private lateinit var mImageAdapter: ImageVideoListAdapter
    private val DESC: String = "DESC"
    private val ASC: String = "ASC"

    private var currentOrder = DESC

    private val ALL: String = "ALL"
    private val NONE: String = "NONE"
    private var currentSelect = NONE
    private var mPageType: String = Common.IMAGE_LIST
    private var deletedFile: Boolean? = false
    private var registerForActivityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            deletedFile = it.data?.getBooleanExtra("deleteResult", false)
            if (deletedFile == true) {
                orderShowImageList()
            }
            setResult()
        }
    }

    private fun setResult() {
        if (deletedFile == true) {
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
        viewModel = ViewModelProvider(this)[ImageListVM::class.java]
        binding.titleBar.setLeftClickListener {
            setResult()
            finish()
        }
        binding.titleBar.setOrderVisible(true)
        binding.titleBar.setOrderClickListener {
            currentOrder = when (currentOrder) {
                DESC -> ASC
                ASC -> DESC
                else -> {
                    ""
                }
            }
            orderShowImageList()
        }
        binding.vImageDelete.visibility = View.GONE
        binding.titleBar.setSelectClickListener {
            when (currentSelect) {
                NONE -> {
                    selectAllShowImageList()
                    currentSelect = ALL
                    binding.titleBar.setSelectText("unselect")
                }

                ALL -> {
                    unselectAllShowImageList()
                    currentSelect = NONE
                    binding.titleBar.setSelectText("select all")
                }
            }
        }
        mPageType = intent.getStringExtra(Common.PAGE_TYPE).toString()
        orderShowImageList()
        viewModel.listSelectCount.observe(this) {
            if (it > 0) {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_selected)
            } else {
                binding.vImageDelete.setBackgroundResource(R.drawable.bg_delete_normal)
            }
        }
        viewModel.imageList.observe(this) {
            getImageViewShowList()
        }
        binding.vImageDelete.setOnClickListener {
            showDeleteDialog()
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
        for (file in showImageList) {
            if (file.selected) {
                file.path?.let { FileUtils.deleteFile(it) }
            }
        }
        mImageAdapter.touchState = mImageAdapter.CLICKSTATE
        viewModel.listSelectCount.value = 0
        binding.titleBar.setOrderVisible(true)
        binding.vImageDelete.visibility = View.GONE
        setResult(RESULT_OK)
    }

    private fun unselectAllShowImageList() {
        viewModel.listSelectCount.value = 0
        for (file in showImageList) {
            file.selected = false
        }
        mImageAdapter.setData(showImageList)
        mImageAdapter.notifyDataSetChanged()
    }

    private fun selectAllShowImageList() {
        viewModel.listSelectCount.value = 0
        for (file in showImageList) {
            if (!file.isTitle) {
                file.selected = true
                viewModel.listSelectCount.value = viewModel.listSelectCount.value!! + 1
            }
        }
        mImageAdapter.setData(showImageList)
        mImageAdapter.notifyDataSetChanged()
    }

    private fun orderShowImageList() {
        viewModel.imageList.value = null
        when (currentOrder) {
            DESC -> {
                when (mPageType) {
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

            ASC -> {
                when (mPageType) {
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
        showImageList = viewModel.changeShowImageList(viewModel.imageList.value)
        mImageAdapter.setData(showImageList)
        mImageAdapter.notifyDataSetChanged()
    }

    private fun setImageListAdapter() {

        val manager = GridLayoutManager(this, 3, LinearLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (showImageList[position].isTitle) {
                    3
                } else {
                    1
                }
            }
        }
        binding.rvImageList.layoutManager = manager
        binding.rvImageList.addItemDecoration(MediaItemDecoration(6))
        mImageAdapter = ImageVideoListAdapter(this, showImageList, { position, touchView ->
            if (mImageAdapter.touchState == mImageAdapter.LONGSTATE
                && touchView != mImageAdapter.TOUCHTITLEVIEW
            ) {
                if (showImageList[position].selected) {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.minus(1)
                    currentSelect = NONE
                    binding.titleBar.setSelectText("select all")
                } else {
                    viewModel.listSelectCount.value = viewModel.listSelectCount.value?.plus(1)
                    if (viewModel.listSelectCount.value == viewModel.imageList.value?.size) {
                        currentSelect = ALL
                        binding.titleBar.setSelectText("unselect")
                    }
                }
                showImageList[position].selected = !showImageList[position].selected
                mImageAdapter.setData(showImageList)
                mImageAdapter.notifyItemChanged(position)
            } else {
                when (touchView) {
                    mImageAdapter.TOUCHIMAGEVIEW -> {
                        toPreviewImage(position)
                    }

                    mImageAdapter.TOUCHPLAYVIEW -> {
                        toPreviewImage(position)
                    }
                }
            }
        }, { position ->
            if (!showImageList[position].selected && mImageAdapter.touchState != mImageAdapter.LONGSTATE) {
                mImageAdapter.touchState = mImageAdapter.LONGSTATE
                showImageList[position].selected = !showImageList[position].selected
                mImageAdapter.setData(showImageList)
                binding.vImageDelete.visibility = View.VISIBLE
                viewModel.listSelectCount.value = 1
                binding.titleBar.setSelectVisible(true)
                mImageAdapter.notifyDataSetChanged()
            }
        })
        binding.rvImageList.adapter = mImageAdapter

    }

    private fun toPreviewImage(position: Int) {
        registerForActivityResult.launch(Intent(this, PreviewActivity::class.java).apply {
            putExtra("selectImageIndex", position - viewModel.getEmptyData(position, showImageList))
            putExtra("previewFileList", Gson().toJson(viewModel.imageList.value))
            putExtra(Common.PAGE_TYPE, mPageType)
        })
    }

    override fun onBackPressed() {
        if (viewModel.listSelectCount.value!! > 0 || mImageAdapter.touchState == mImageAdapter.LONGSTATE) {
            mImageAdapter.touchState = mImageAdapter.CLICKSTATE
            unselectAllShowImageList()
            binding.titleBar.setOrderVisible(true)
            binding.vImageDelete.visibility = View.GONE
        } else {
            super.onBackPressed()
            setResult()
        }
    }
}