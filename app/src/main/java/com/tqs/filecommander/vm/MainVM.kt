package com.tqs.filecommander.vm

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filecommander.adapter.DocAdapter
import com.tqs.filecommander.adapter.ImageVideoListAdapter
import com.tqs.filecommander.adapter.PreviewAdapter
import com.tqs.filecommander.base.BaseAds
import com.tqs.filecommander.base.BaseVM
import com.tqs.filecommander.model.DocumentEntity
import com.tqs.filecommander.model.FileEntity
import com.tqs.filecommander.ui.view.ConfirmAndCancelDialog
import com.tqs.filecommander.ui.view.FileDetailPopupWindow
import com.tqs.filecommander.utils.Common
import com.tqs.filecommander.utils.DateUtils
import com.tqs.filecommander.utils.FileUtils

class MainVM : BaseVM() {

    override lateinit var title: LiveData<String>

    val DESC: String = "DESC"
    val ASC: String = "ASC"
    val ALL: String = "ALL"
    val NONE: String = "NONE"

    var currentIndex = 0
    var baseAds: BaseAds? = null
    val countDownTime = 8 * 1000L
    var deletedFile: Boolean = false
    var mPageType: String = Common.IMAGE_LIST

    lateinit var mDocAdapter: DocAdapter
    lateinit var mPreviewAdapter: PreviewAdapter
    lateinit var mImageAdapter: ImageVideoListAdapter

    var dataList = MutableLiveData<ArrayList<FileEntity>>()
    var listSelectCount = MutableLiveData<Int>(0)

    var previewMediaList: MutableList<FileEntity>? = null
    var docDataList: ArrayList<DocumentEntity> = arrayListOf()
    var showDataList: ArrayList<FileEntity> = arrayListOf()

    var mDeleteDialog: ConfirmAndCancelDialog? = null
    var mPopupWindow: FileDetailPopupWindow? = null

    var currentOrder = MutableLiveData<String>(DESC)
    var currentSelect = MutableLiveData<String>(NONE)

    fun getImageList(context: Context) {
        getImageListOrderDescByDate(context)
    }

    fun getVideoList(context: Context) {
        getVideoListOrderDescByDate(context)
    }

    fun getAudioList(context: Context) {
        dataList.value = FileUtils.getAudioList(context)
    }

    fun getDocumentsList(context: Context) {
        dataList.value = FileUtils.getDocList(context)
    }

    fun getDownloadList(context: Context) {
        dataList.value = FileUtils.getDownloadList(context)
    }

    private fun getVideoListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderDescByDate(context)
    }

    private fun getVideoListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderAscByDate(context)
    }

    private fun getImageListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    private fun getImageListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderAscByDate(context)
    }

    private fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
        val showDataList: MutableList<FileEntity> = mutableListOf()
        var date = ""
        if (list != null) {
            for (fileEntity in list) {
                val yyMM = DateUtils.getMonthTimeBySecond(fileEntity.date)
                if (yyMM != date) {
                    showDataList.add(FileEntity(isTitle = true, date = fileEntity.date))
                    date = yyMM
                }
                showDataList.add(fileEntity)
            }
        }
        return showDataList as ArrayList<FileEntity>
    }

    fun getEmptyData(position: Int, showDataList: ArrayList<FileEntity>?): Int {
        var count: Int = 0
        for (i in 0..showDataList?.size!!) {
            if (i <= position && showDataList[i].isTitle) {
                count++
            }
        }
        return count
    }

    fun unselectAllShowImageList() {
        listSelectCount.value = 0
        for (file in showDataList) {
            file.selected = false
        }
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    fun getImageViewShowList() {
        showDataList = changeShowImageList(dataList.value)
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    private fun selectAllShowImageList() {
        listSelectCount.value = 0
        for (file in showDataList) {
            if (!file.isTitle) {
                file.selected = true
                listSelectCount.value = listSelectCount.value!! + 1
            }
        }
        mImageAdapter.setData(showDataList)
        mImageAdapter.notifyDataSetChanged()
    }

    fun orderShowImageList(context: Context, title: (String) -> Unit) {
        dataList.value?.clear()
        when (currentOrder.value) {
            DESC -> {
                when (mPageType) {
                    Common.IMAGE_LIST -> {
                        getImageListOrderDescByDate(context)
                        title.invoke("Image")
                    }

                    Common.VIDEO_LIST -> {
                        getVideoListOrderDescByDate(context)
                        title.invoke("Video")
                    }
                }
            }

            ASC -> {
                when (mPageType) {
                    Common.IMAGE_LIST -> {
                        getImageListOrderAscByDate(context)
                        title.invoke("Image")
                    }

                    Common.VIDEO_LIST -> {
                        getVideoListOrderAscByDate(context)
                        title.invoke("Video")
                    }
                }
            }
        }
    }

    fun showDeleteDialog(context: Context, title: (String) -> Unit, function: () -> Unit) {
        if (mDeleteDialog == null) {
            mDeleteDialog = ConfirmAndCancelDialog(context, {
                mDeleteDialog?.dismiss()
            }, {
                mDeleteDialog?.dismiss()
                deleteSelectedImage(function)
                orderShowImageList(context, title)
            })

        }
        mDeleteDialog?.show()
    }

    private fun deleteSelectedImage(function: () -> Unit) {
        for (file in showDataList) {
            if (file.selected) {
                file.path?.let { FileUtils.deleteFile(it) }
            }
        }
        mImageAdapter.touchState = mImageAdapter.CLICKSTATE
        listSelectCount.value = 0
        function.invoke()
    }

    fun onImageAdapterItemClick(touchView: String, position: Int, title: (String) -> Unit, preview: (Int) -> Unit) {
        if (mImageAdapter.touchState == mImageAdapter.LONGSTATE
            && touchView != mImageAdapter.TOUCHTITLEVIEW
        ) {
            if (showDataList[position].selected) {
                listSelectCount.value = listSelectCount.value?.minus(1)
                currentSelect.value = NONE
                title.invoke("select all")
            } else {
                listSelectCount.value = listSelectCount.value?.plus(1)
                if (listSelectCount.value == dataList.value?.size) {
                    currentSelect.value = ALL
                    title.invoke("unselect")
                }
            }
            showDataList[position].selected = !showDataList[position].selected
            mImageAdapter.setData(showDataList)
            mImageAdapter.notifyItemChanged(position)
        } else {
            when (touchView) {
                mImageAdapter.TOUCHIMAGEVIEW -> {
                    preview.invoke(position)
                }

                mImageAdapter.TOUCHPLAYVIEW -> {
                    preview.invoke(position)
                }
            }
        }
    }

    fun onImageAdapterItemLongClick(position:Int,viewFun: () -> Unit) {
        if (!showDataList[position].selected && mImageAdapter.touchState != mImageAdapter.LONGSTATE) {
            mImageAdapter.touchState = mImageAdapter.LONGSTATE
            showDataList[position].selected = !showDataList[position].selected
            mImageAdapter.setData(showDataList)
            viewFun.invoke()
            listSelectCount.value = 1
            mImageAdapter.notifyDataSetChanged()
        }
    }

    fun setSelectText(function: (String) -> Unit) {
        when (currentSelect.value) {
            NONE -> {
                selectAllShowImageList()
                currentSelect.value = ALL
                function.invoke("unselect")
            }

            ALL -> {
                unselectAllShowImageList()
                currentSelect.value = NONE
                function.invoke("select all")
            }
        }
    }

}