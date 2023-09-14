package com.tqs.filemanager.vm.activity

import android.content.Context
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.base.BaseVM
import com.tqs.filemanager.vm.utils.DateUtils
import com.tqs.filemanager.vm.utils.FileUtils

class ImageListVM : BaseVM() {
    var imageList = MutableLiveData<ArrayList<FileEntity>>()
    var listSelectCount = MutableLiveData<Int>(0)
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "ImageListVM"
        }

    fun getVideoListOrderDescByDate(context: Context) {
        imageList.value = FileUtils.getVideoListOrderDescByDate(context)
    }
    fun getVideoListOrderAscByDate(context: Context) {
        imageList.value = FileUtils.getVideoListOrderAscByDate(context)
    }

    fun getImageListOrderDescByDate(context: Context) {
        imageList.value = FileUtils.getImgListOrderDescByDate(context)
    }

    fun getImageListOrderAscByDate(context: Context) {
        imageList.value = FileUtils.getImgListOrderAscByDate(context)
    }

    fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
        val showImageList: MutableList<FileEntity> = mutableListOf()
        var date = ""
        if (list != null) {
            for (fileEntity in list) {
                val yyMM = DateUtils.getMonthTimeBySecond(fileEntity.date)
                if (yyMM != date) {
                    showImageList.add(FileEntity(isTitle = true, date = fileEntity.date))
                    date = yyMM
                }
                showImageList.add(fileEntity)
            }
        }
        return showImageList as ArrayList<FileEntity>
    }

    fun getEmptyData(position: Int, showImageList: ArrayList<FileEntity>?): Int {
        var count: Int = 0
        for (i in 0..showImageList?.size!!) {
            if (i <= position && showImageList[i].isTitle) {
                count++
            }
        }
        Log.e("", "count = $count")
        return count
    }


}