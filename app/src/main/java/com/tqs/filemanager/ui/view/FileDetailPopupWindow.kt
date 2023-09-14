package com.tqs.filemanager.ui.view

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.DateUtils
import com.tqs.filemanager.vm.utils.FileUtils

class FileDetailPopupWindow(private val context: Context) : PopupWindow() {
    private var fileName: TextView
    private var fileSize: TextView
    private var fileDate: TextView
    private var fileType: TextView
    private var filePath: TextView

    init {
        height = ViewGroup.LayoutParams.WRAP_CONTENT
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        isOutsideTouchable = true
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        contentView =
            LayoutInflater.from(context).inflate(R.layout.popupwindow_file_detail, null, false)
        fileName = contentView.findViewById<TextView>(R.id.tv_file_name)
        fileSize = contentView.findViewById<TextView>(R.id.tv_file_size)
        fileDate = contentView.findViewById<TextView>(R.id.tv_file_date)
        fileType = contentView.findViewById<TextView>(R.id.tv_file_type)
        filePath = contentView.findViewById<TextView>(R.id.tv_file_path)
    }

    fun setFileInfo(
        name: String = "",
        size: String = "",
        date: String = "",
        type: String = "",
        path: String = ""
    ) {
        fileName.text = name
        fileSize.text = size
        fileDate.text = date
        fileType.text = type
        filePath.text = path
    }
    fun setFileInfo(fileEntity: FileEntity
    ) {
        fileName.text = "${fileEntity.name}"
        fileSize.text = FileUtils.getTwoDigitsSpace(fileEntity.size)
        fileDate.text = DateUtils.getDateTimeBySecond(fileEntity.date)
        fileType.text = fileEntity.mimeType
        filePath.text = fileEntity.path

    }
}