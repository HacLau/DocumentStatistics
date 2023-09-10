package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import java.io.File

class ImageListAdapter(
    private val context: Context,
    private val data: List<FileEntity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE = 0
    private val CONTENT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when (viewType) {
            TITLE -> {
                TitleViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_title, parent, false)
                )
            }

            CONTENT -> {
                ContentViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_photo, parent, false)
                )
            }

            else -> {
                ContentViewHolder(
                    LayoutInflater.from(context).inflate(R.layout.media_item_photo, parent, false)
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TITLE -> {
                if (holder is TitleViewHolder) {
                    holder.titleDate.text = data[position].dateString
                }
            }

            CONTENT -> {
                if (holder is ContentViewHolder) {
                    holder.contentImage.setImageURI(Uri.fromFile(File(data[position].path)))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (TextUtils.isEmpty(data[position].dateString)) {
            CONTENT
        } else {
            TITLE
        }
    }


    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDate = view.findViewById<TextView>(R.id.item_title)
        var titleSelectAll = view.findViewById<TextView>(R.id.item_select_all)

        init {
            titleSelectAll.setOnClickListener {

            }
        }
    }

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contentImage = view.findViewById<ImageView>(R.id.item_image)

        init {
            contentImage.setOnClickListener {

            }

            contentImage.setOnLongClickListener(object : OnLongClickListener {
                override fun onLongClick(v: View?): Boolean {
                    return true
                }

            })
        }
    }

}