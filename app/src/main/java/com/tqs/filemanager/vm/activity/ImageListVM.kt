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
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "ImageListVM"
        }

    fun getVideoList(context: Context) {
        imageList.value = FileUtils.getVideoList(context)
    }

    fun getImageList(context: Context) {
        imageList.value = FileUtils.getImgList(context)
    }

    fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
        val showImageList: MutableList<FileEntity> = mutableListOf()
        var date = ""
        if (list != null) {
            for (fileEntity in list) {
                Log.e("Image", "${fileEntity.date}")
                val yyMM = DateUtils.second2yyMM(fileEntity.date)
                if (yyMM != date) {
                    showImageList.add(FileEntity(dateString = yyMM))
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
            if (i <= position && !TextUtils.isEmpty(showImageList[i].dateString)) {
                count++
            }
        }
        Log.e("", "count = $count")
        return count
    }


}