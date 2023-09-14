package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.DateUtils
import com.tqs.filemanager.vm.utils.FileUtils
import com.tqs.filemanager.vm.utils.VideoUtils
import java.io.File

class ImageVideoListAdapter(
    private val context: Context,
    private var data: List<FileEntity>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TITLE = 0
    private val CONTENT = 1

    private var mOnItemClickListener: OnItemClickListener? = null
    private var mOnItemLongClickListener: OnItemLongClickListener? = null

    val CLICKSTATE: Int = 11
    val LONGSTATE: Int = 12

    // 1 click 2 Long click
    var touchState: Int = CLICKSTATE

    val TOUCHTITLEVIEW = "touchTitleView"
    val TOUCHSELECTVIEW = "touchSelectView"
    val TOUCHRADIOVIEW = "touchRadioView"
    val TOUCHIMAGEVIEW = "touchImageView"
    val TOUCHPLAYVIEW = "touchPlayView"

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
                    holder.titleDate.text = DateUtils.getMonthTimeBySecond(data[position].date)

                    holder.titleDate.setOnClickListener {
                        mOnItemClickListener?.onItemClick(position, TOUCHTITLEVIEW)
                    }
                }
            }

            CONTENT -> {
                if (holder is ContentViewHolder) {
                    when (data[position].fileType) {
                        FileUtils.TYPEIMAGE -> {
                            holder.contentImage.setImageURI(Uri.fromFile(data[position].path?.let {
                                File(it)
                            }))
                            holder.itemPlay.visibility = View.GONE
                        }

                        FileUtils.TYPEVIDEO -> {
                            holder.contentImage.setImageBitmap(data[position].path?.let {
                                VideoUtils.getBitmap(it)
                            })
                            holder.itemPlay.visibility = View.VISIBLE
                        }
                    }
                    holder.imageSize.text = FileUtils.getTwoDigitsSpace(data[position].size)
                    if (touchState == LONGSTATE){
                        holder.selectedImage.visibility = View.VISIBLE
                    }else{
                        holder.selectedImage.visibility = View.GONE
                    }
                    holder.selectedImage.isChecked = data[position].selected
                    holder.contentImage.setOnClickListener {
                        mOnItemClickListener?.onItemClick(position, TOUCHIMAGEVIEW)
                    }
                    holder.contentImage.setOnLongClickListener {
                        mOnItemLongClickListener?.onItemLongClick(position)
                        true
                    }

                    holder.selectedImage.setOnClickListener {
                        mOnItemClickListener?.onItemClick(position, TOUCHRADIOVIEW)
                    }

                    holder.itemPlay.setOnClickListener {
                        mOnItemClickListener?.onItemClick(position, TOUCHPLAYVIEW)
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position].isTitle) {
            TITLE
        } else {
            CONTENT
        }
    }


    inner class TitleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var titleDate = view.findViewById<TextView>(R.id.item_title)

    }

    inner class ContentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var contentImage = view.findViewById<ImageView>(R.id.item_image)
        var selectedImage = view.findViewById<CheckBox>(R.id.item_selected)
        var itemPlay = view.findViewById<ImageView>(R.id.item_play)
        var imageSize = view.findViewById<TextView>(R.id.item_size)
    }

    fun setData(list: ArrayList<FileEntity>) {
        this.data = list
    }


    public fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    public fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.mOnItemLongClickListener = listener
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, touchView: String)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(position: Int)
    }

}