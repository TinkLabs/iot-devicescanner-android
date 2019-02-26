package com.tinklabs.iot.devicescanner.widget

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialog
import com.tinklabs.iot.devicescanner.R

class LoadingDialogFactory constructor(context: Context){
    private var mDialog: AppCompatDialog = AppCompatDialog(context)

    init {
        create(context)
    }

    private fun create(context: Context): AppCompatDialog {
        val inflater = LayoutInflater.from(context)
        val rootView = inflater.inflate(R.layout.loading_layout,null)
        mDialog.setContentView(rootView)
        mDialog.window!!.setBackgroundDrawableResource(R.drawable.loading_background)
        return mDialog
    }

    fun show() {
        mDialog.show()
    }

    fun dismiss() {
        mDialog.dismiss()
    }
}