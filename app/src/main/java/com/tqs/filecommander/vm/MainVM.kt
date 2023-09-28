package com.tqs.filecommander.vm

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
    var mDataList: ArrayList<DocumentEntity> = arrayListOf()
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

    fun getVideoListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderDescByDate(context)
    }

    fun getVideoListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getVideoListOrderAscByDate(context)
    }

    fun getImageListOrderDescByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    fun getImageListOrderAscByDate(context: Context) {
        dataList.value = FileUtils.getImgListOrderAscByDate(context)
    }

    fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
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

}