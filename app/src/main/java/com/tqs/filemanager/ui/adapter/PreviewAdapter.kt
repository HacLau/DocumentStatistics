package com.tqs.filemanager.ui.adapter

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnErrorListener
import android.net.Uri
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.viewpager.widget.PagerAdapter
import com.tqs.document.statistics.R
import com.tqs.filemanager.model.FileEntity
import com.tqs.filemanager.vm.utils.FileUtils
import com.tqs.filemanager.vm.utils.VideoUtils
import java.io.File

class PreviewAdapter(private val context: Context, private val data: MutableList<FileEntity>?) :
    PagerAdapter(), SurfaceHolder.Callback {
    private var mClickPlayListener: OnClickPlayListener? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var currentPlayView: ImageView? = null
    private var currentSurfaceView: SurfaceView? = null
    private var currentProgressBar: ProgressBar? = null
    private var currentImageView: ImageView? = null
    private var currentPosition: Int? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = View.inflate(context, R.layout.media_preview, null)
        val imageView = view.findViewById<ImageView>(R.id.iv_preview_image)
        val playView = view.findViewById<ImageView>(R.id.item_preview_play)
        val surfaceView = view.findViewById<SurfaceView>(R.id.sv_play_video)
        val progressBar = view.findViewById<ProgressBar>(R.id.pb_video)
        when (data?.get(position)?.fileType) {
            FileUtils.TYPEIMAGE -> {
                imageView.setImageURI(Uri.fromFile(data[position].path?.let { File(it) }))
                playView.visibility = View.GONE
                surfaceView.visibility = View.GONE
                imageView.visibility = View.VISIBLE
            }

            FileUtils.TYPEVIDEO -> {
                imageView.setImageBitmap(data[position].path?.let { VideoUtils.getBitmap(it) })
                imageView.visibility = View.VISIBLE
                playView.visibility = View.VISIBLE
                surfaceView.visibility = View.GONE
            }
        }
        playView.setOnClickListener {
            currentPlayView = playView
            currentProgressBar = progressBar
            currentImageView= imageView
            currentSurfaceView = surfaceView
            currentPosition = position
            setPlayMedia()
            playView.visibility = View.GONE
            imageView.visibility = View.GONE
            mClickPlayListener?.playVideo(surfaceView, position)

        }

        container.addView(view)
        return view
    }

    private fun setPlayMedia() {
        if (mMediaPlayer == null) {
            mMediaPlayer = MediaPlayer()
        }
        val holder = currentSurfaceView?.holder
        holder?.addCallback(this)
        mMediaPlayer?.setOnPreparedListener {
            currentProgressBar?.max = mMediaPlayer?.duration!!
            currentProgressBar?.progress = mMediaPlayer?.currentPosition!!
            currentSurfaceView?.visibility = View.VISIBLE
            mMediaPlayer?.start()
        }
        mMediaPlayer?.setOnCompletionListener {
            stopPlayer()
        }
        val bitmap = currentPosition?.let { data?.get(it)?.path?.let { VideoUtils.getBitmap(it) } }
        val bitmapWidth = bitmap?.width
        val bitmapHeight = bitmap?.height
        var screenWidth = context.resources.displayMetrics.widthPixels
        val layoutParams = currentSurfaceView?.layoutParams
        if (bitmapWidth != null && bitmapHeight != null) {
            layoutParams?.width = screenWidth
            layoutParams?.height = bitmapHeight * screenWidth / bitmapWidth
        }

        mMediaPlayer?.setDataSource(currentPosition?.let { data?.get(it)?.path })
        mMediaPlayer?.prepare()
    }

    fun stopPlayer() {
        if (mMediaPlayer?.isPlaying == true) {
            mMediaPlayer?.stop()
        }
        currentPlayView?.visibility = View.VISIBLE
        currentImageView?.visibility = View.VISIBLE
        currentSurfaceView?.visibility = View.GONE
        mMediaPlayer?.release()
        mMediaPlayer = null

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
        fun playVideo(surfaceView: SurfaceView, position: Int)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.e("setPlayer", "surfaceCreated")
        mMediaPlayer?.setDisplay(holder)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        Log.e("setPlayer", "surfaceChanged")

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.e("setPlayer", "surfaceDestroyed")
        mMediaPlayer?.release()
        mMediaPlayer = null
    }
}