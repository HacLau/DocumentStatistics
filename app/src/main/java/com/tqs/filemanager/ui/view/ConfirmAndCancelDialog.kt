package com.tqs.filemanager.ui.view

import android.R.attr.x
import android.R.attr.y
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.View.OnClickListener
import android.widget.TextView
import com.tqs.document.statistics.R


class ConfirmAndCancelDialog(context: Context) : Dialog(context), View.OnClickListener {
    private lateinit var mTitle: TextView
    private lateinit var mContent: TextView
    private lateinit var mCancel: TextView
    private lateinit var mSure: TextView

    private var sTitle: String? = null
    private var sContent: String? = null
    private var sCancel: String? = null
    private var sSure: String? = null

    private lateinit var cancelOnClickListener: OnClickListener
    private lateinit var sureOnClickListener: OnClickListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_confirm_cancel)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        val wm = window?.windowManager
        val defaultDisplay = wm?.defaultDisplay
        val p = window?.attributes
        val point = Point()
        defaultDisplay?.getSize(point)
        p?.width = (point.x * 0.8).toInt()
        window?.attributes = p

        mTitle = findViewById(R.id.dialog_title)
        mContent = findViewById(R.id.dialog_content)
        mCancel = findViewById(R.id.dialog_cancel)
        mSure = findViewById(R.id.dialog_sure)

        mTitle.text = if (TextUtils.isEmpty(sTitle)) {
            "warning"
        } else {
            sTitle
        }
        mContent.text = if (TextUtils.isEmpty(sContent)) {
            "are you sure?"
        } else {
            sContent
        }
        mCancel.text = if (TextUtils.isEmpty(sCancel)) {
            "cancel"
        } else {
            sCancel
        }
        mSure.text = if (TextUtils.isEmpty(sSure)) {
            "sure"
        } else {
            sSure
        }

        mCancel.setOnClickListener(this)
        mSure.setOnClickListener(this)
    }

    fun setTitle(string: String): ConfirmAndCancelDialog {
        this.sTitle = string
        return this
    }

    fun setContent(string: String): ConfirmAndCancelDialog {
        this.sContent = string
        return this
    }

    fun setCancel(string: String): ConfirmAndCancelDialog {
        this.sCancel = string
        return this
    }

    fun setSure(string: String): ConfirmAndCancelDialog {
        this.sSure = string
        return this
    }

    fun setCancelClickListener(listener: OnClickListener){
        this.cancelOnClickListener = listener
    }

    fun setSureClickListener(listener: OnClickListener){
        this.sureOnClickListener = listener
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.dialog_cancel -> {
                cancelOnClickListener.onClick(v)
            }

            R.id.dialog_sure -> {
                sureOnClickListener.onClick(v)
            }
        }
    }
}