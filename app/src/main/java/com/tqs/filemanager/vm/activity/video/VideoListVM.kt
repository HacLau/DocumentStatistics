package com.tqs.filemanager.vm.activity.video

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.base.BaseVM
import com.tqs.filemanager.vm.utils.DateUtils
import com.tqs.filemanager.vm.utils.FileManagerUtil

class VideoListVM : BaseVM() {
    var videoList = MutableLiveData<ArrayList<FileEntity>>()
    override val title: LiveData<String>
        get() = MutableLiveData<String>().apply {
            value = "VideoListVM"
        }

    fun getImageList(context: Context) {
        videoList.value = FileManagerUtil.getVideoList(context)
    }

    fun changeShowImageList(list: ArrayList<FileEntity>?): ArrayList<FileEntity> {
        val showImageList: MutableList<FileEntity> = mutableListOf()
        var date = ""
        if (list != null) {
            for (fileEntity in list) {
                Log.e("Video" , "${fileEntity.date}")
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
}