package com.tinklabs.iot.devicescanner.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import com.tinklabs.iot.devicescanner.R
import kotlinx.android.synthetic.main.confirm_dialog.view.*

class ConfirmDialog(context: Context) : AppCompatDialog(context, R.style.ConfirmDialog) {
    private val tvTitle: AppCompatTextView
    private val tvMessage: AppCompatTextView
    private val btnCancel: AppCompatTextView
    private val btnConfirm: AppCompatTextView

    init {
        val inflater = LayoutInflater.from(context)
        val rootView = inflater.inflate(R.layout.confirm_dialog, null)
        tvTitle = rootView.tvTitle
        tvMessage = rootView.tvMessage
        btnCancel = rootView.btnCancel
        btnConfirm = rootView.btnConfirm

        onCancel(null)
        onConfirm(null)

        window!!.setBackgroundDrawableResource(R.drawable.dialog_background)
        setContentView(rootView)

        val params = window!!.attributes
        params.width = (context.resources.displayMetrics.widthPixels * 0.7).toInt()
        window!!.attributes = params as android.view.WindowManager.LayoutParams
        window!!.decorView.setPadding(0, 0, 0, 0)
    }

    fun confirmText(titleId: Int): ConfirmDialog {
        btnConfirm.setText(titleId)
        return this
    }

    fun confirmText(title: String): ConfirmDialog {
        btnConfirm.text = title
        return this
    }

    fun confirmTextColor(colorRes: Int): ConfirmDialog {
        btnConfirm.setTextColor(ActivityCompat.getColor(context, colorRes))
        return this
    }

    fun cancelText(titleId: Int): ConfirmDialog {
        btnCancel.setText(titleId)
        return this
    }

    fun cancelText(title: String): ConfirmDialog {
        btnCancel.text = title
        return this
    }

    fun cancelTextColor(colorRes: Int): ConfirmDialog {
        btnCancel.setTextColor(ActivityCompat.getColor(context, colorRes))
        return this
    }

    fun title(titleId: Int): ConfirmDialog {
        tvTitle.setText(titleId)
        return this
    }

    fun title(title: String): ConfirmDialog {
        tvTitle.text = title
        return this
    }

    fun content(titleId: Int): ConfirmDialog {
        tvMessage.setText(titleId)
        return this
    }

    fun content(title: String): ConfirmDialog {
        tvMessage.text = title
        return this
    }

    fun onConfirm(listener: View.OnClickListener?): ConfirmDialog {
        btnConfirm.setOnClickListener {
            listener?.onClick(btnConfirm)
            dismiss()
        }
        return this
    }

    fun onCancel(listener: View.OnClickListener?): ConfirmDialog {
        btnCancel.setOnClickListener {
            listener?.onClick(btnCancel)
            dismiss()
        }
        return this
    }

    fun cancelable(enable: Boolean): ConfirmDialog {
        setCancelable(enable)
        return this
    }
}