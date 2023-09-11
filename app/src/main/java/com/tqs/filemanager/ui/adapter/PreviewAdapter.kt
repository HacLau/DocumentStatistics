package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.viewpager.widget.PagerAdapter
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.FileUtils
import com.tqs.filemanager.vm.utils.VideoUtils
import java.io.File

class PreviewAdapter(private val context: Context, private val data: MutableList<FileEntity>?) :
    PagerAdapter() {
    private var mClickPlayListener: OnClickPlayListener? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(context, R.layout.media_preview, null)
        val imageView = view.findViewById<ImageView>(R.id.iv_preview_image)
        val playView = view.findViewById<ImageView>(R.id.item_preview_play)
        val videoView = view.findViewById<VideoView>(R.id.vv_preview_video)
        when (data?.get(position)?.fileType) {
            FileUtils.TYPEIMAGE -> {
                imageView.setImageURI(Uri.fromFile(data[position].path?.let { File(it) }))
                playView.visibility = View.GONE
                videoView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
            }

            FileUtils.TYPEVIDEO -> {
                imageView.setImageBitmap(data[position].path?.let { VideoUtils.getBitmap(it) })
                videoView.setVideoPath(data[position].path)
                playView.visibility = View.VISIBLE
                videoView.visibility = View.VISIBLE
                imageView.visibility = View.GONE
                val controller = MediaController(context)
                videoView.setMediaController(controller)
                videoView.requestFocus()
            }
        }
        playView.setOnClickListener {
            videoView.start()
            mClickPlayListener?.playVideo(position)
        }
        container.addView(view)
        return view
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    fun setOnClickPlayListener(listener: OnClickPlayListener) {
        this.mClickPlayListener = listener
    }

    interface OnClickPlayListener {
        fun playVideo(position: Int)
    }
}