package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.DateUtils

class DocAdapter(
    private val context: Context,
    private val data: ArrayList<FileEntity>): BaseAdapter() {
    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var holder:ViewHolder? = null
        var view:View
        if (convertView == null){
            view = View.inflate(context, R.layout.file_item, null)
            holder = ViewHolder(view)
            view.tag = holder
        }else{
            view = convertView
            holder = view.tag as ViewHolder
        }
        val fileEntity = data[position]
        holder.date.text = DateUtils.second2yyMM(fileEntity.date)
        return view
    }

    inner class ViewHolder(view: View){
        var icon = view.findViewById<ImageView>(R.id.item_file_icon)
        var title = view.findViewById<TextView>(R.id.item_file_title)
        var date = view.findViewById<TextView>(R.id.item_file_date)
        var select = view.findViewById<CheckBox>(R.id.item_file_select)
    }
}