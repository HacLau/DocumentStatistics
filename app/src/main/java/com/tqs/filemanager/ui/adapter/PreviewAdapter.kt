package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.tqs.document.statistics.R
import com.tqs.document.statistics.databinding.MediaPreviewBinding
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.FileUtils
import com.tqs.filemanager.vm.utils.VideoUtils
import java.io.File

class PreviewAdapter(
    private val context: Context,
    private var data: MutableList<FileEntity>?,
    private var playVideo: (surfaceView: SurfaceView, position: Int) -> Unit
) :
    PagerAdapter(), SurfaceHolder.Callback {
    private var mMediaPlayer: MediaPlayer? = null
    private var currentPosition: Int? = null
    private var currentBinding: MediaPreviewBinding? = null
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val binding: MediaPreviewBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.media_preview, null, false)
        when (data?.get(position)?.fileType) {
            FileUtils.TYPEIMAGE -> {
                binding.let {
                    it.ivPreviewImage.setImageURI(Uri.fromFile(data!![position].path?.let { path -> File(path) }))
                    it.itemPreviewPlay.visibility = View.GONE
                    it.svPlayVideo.visibility = View.GONE
                    it.ivPreviewImage.visibility = View.VISIBLE
                }
            }

            FileUtils.TYPEVIDEO -> {
                binding.let {
                    it.ivPreviewImage.setImageBitmap(data!![position].path?.let { path -> VideoUtils.getBitmap(path) })
                    it.ivPreviewImage.visibility = View.VISIBLE
                    it.itemPreviewPlay.visibility = View.VISIBLE
                    it.svPlayVideo.visibility = View.GONE
                }
            }
        }
        binding.itemPreviewPlay.setOnClickListener {
            currentBinding = binding
            currentPosition = position
            setPlayMedia()

            playVideo(binding.svPlayVideo, position)

        }

        container.addView(binding.root)
        return binding.root
    }

    private fun setPlayMedia() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
        mMediaPlayer?.let { it ->
            val holder = currentBinding?.svPlayVideo?.holder
            holder?.addCallback(this@PreviewAdapter)
            it.setOnPreparedListener {
                currentBinding?.let { binding ->
                    binding.itemPreviewPlay.visibility = View.GONE
                    binding.ivPreviewImage.visibility = View.GONE
                    binding.pbVideo.max = mMediaPlayer?.duration!!
                    binding.pbVideo.progress = it.currentPosition
                    binding.svPlayVideo.visibility = View.VISIBLE
                }
                it.start()
            }
            it.setOnCompletionListener {
                stopPlayer()
            }
            val bitmap = currentPosition?.let { position -> data?.get(position)?.path?.let { VideoUtils.getBitmap(it) } }
            val bitmapWidth = bitmap?.width
            val bitmapHeight = bitmap?.height
            val screenWidth = context.resources.displayMetrics.widthPixels
            currentBinding?.svPlayVideo?.layoutParams?.apply {
                if (bitmapWidth != null && bitmapHeight != null) {
                    width = screenWidth
                    height = bitmapHeight * screenWidth / bitmapWidth
                }
            }
            it.setDataSource(currentPosition?.let { position -> data?.get(position)?.path })
            it.prepare()
        }

    }

    fun stopPlayer() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
        }
        currentBinding?.let {
            it.itemPreviewPlay.visibility = View.VISIBLE
            it.ivPreviewImage.visibility = View.VISIBLE
            it.svPlayVideo.visibility = View.GONE
        }
        mMediaPlayer?.release()
        mMediaPlayer = null
    }

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    // must be this function and return POSITION_NONE can destroy item by dynamic called destroyItem
    override fun getItemPosition(`object`: Any): Int {
//        return super.getItemPosition(`object`)
        return POSITION_NONE
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        Log.e("destroyItem","destroyItem $position")
        container.removeView(`object` as View)
    }

    fun setData(list: MutableList<FileEntity>) {
        this.data = list
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        mMediaPlayer?.setDisplay(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
}