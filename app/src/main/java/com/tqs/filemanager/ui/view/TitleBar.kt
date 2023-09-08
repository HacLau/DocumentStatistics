package com.tqs.filemanager.ui.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tqs.document.statistics.R

class TitleBar : ConstraintLayout {
    private lateinit var mLeftImageView : ImageView
    private lateinit var mTitleTextView :TextView
    private lateinit var mRightImageView: ImageView

    constructor(context: Context) : super(context){
        init(context)
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs){
        init(context)
        setStyle(context,attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs,defStyleAttr){

    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int):super(context, attrs,defStyleAttr,defStyleRes){

    }
    private fun init(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.title_bar, this, true)
        mLeftImageView = view.findViewById<ImageView>(R.id.iv_left)
        mTitleTextView = view.findViewById<TextView>(R.id.tv_title)
        mRightImageView = view.findViewById<ImageView>(R.id.iv_right)
    }

    private fun setStyle(context: Context, attrs: AttributeSet?) {
        if (attrs != null){
            val array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
            val title = array.getString(R.styleable.TitleBar_titleBar_title_text)
            mTitleTextView.text = title
            val titleSize = array.getFloat(R.styleable.TitleBar_titleBar_title_size, 12f)
            mTitleTextView.textSize = titleSize
            val titleColor = array.getColor(R.styleable.TitleBar_titleBar_title_color, Color.BLACK)
            mTitleTextView.setTextColor(titleColor)

            val drawableLeft = array.getDrawable(R.styleable.TitleBar_titleBar_left_src)
            mLeftImageView.setImageDrawable(drawableLeft)
            val visibleLeft = array.getInt(R.styleable.TitleBar_titleBar_left_visible, 0)
            mLeftImageView.visibility = visibleLeft

            val drawableRight = array.getDrawable(R.styleable.TitleBar_titleBar_right_src)
            mRightImageView.setImageDrawable(drawableRight)
            val visibleRight = array.getInt(R.styleable.TitleBar_titleBar_right_visible, 0)
            mLeftImageView.visibility = visibleRight

            array.recycle()
        }
    }

    fun setLeftClickListener(listener: OnClickListener){
        mTitleTextView.setOnClickListener(listener)
    }

    fun setLeftImageResource(resId:Int){
        mLeftImageView.setImageResource(resId)
    }

    fun setRightClickListener(listener: OnClickListener){
        mRightImageView.setOnClickListener(listener)
    }

    fun setRightImageResource(resId:Int){
        mRightImageView.setImageResource(resId)
    }

    fun setTitleText(title:String){
        mTitleTextView.text = title
    }

}