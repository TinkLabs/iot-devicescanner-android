package com.tinklabs.iot.devicescanner.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tinklabs.iot.devicescanner.R

class StatefulLayout : FrameLayout {
    private var mErrorViewRes: Int
    private var mLoadingViewRes: Int
    private var mEmptyViewRes: Int

    companion object {
        const val LOADING_VIEW_INDEX = 0
        const val EMPTY_VIEW_INDEX = 1
        const val ERROR_VIEW_INDEX = 2
    }

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs!!, -1)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        val typeArray = context.theme.obtainStyledAttributes(attrs, R.styleable.StatefulLayout, 0, 0)
        mLoadingViewRes = typeArray.getResourceId(R.styleable.StatefulLayout_loading_view, R.layout.def_loading_view)
        mEmptyViewRes = typeArray.getResourceId(R.styleable.StatefulLayout_empty_view, R.layout.def_empty_view)
        mErrorViewRes = typeArray.getResourceId(R.styleable.StatefulLayout_error_view, R.layout.def_error_view)
        val inflater = LayoutInflater.from(getContext())
        inflater.inflate(mLoadingViewRes, this, true)
        inflater.inflate(mEmptyViewRes, this, true)
        inflater.inflate(mErrorViewRes, this, true)

        typeArray.recycle()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        for (i in 0 until childCount) getChildAt(i).visibility = View.GONE
    }

    fun setEmptyClickListener(handler: () -> Unit) {
        getChildAt(EMPTY_VIEW_INDEX).setOnClickListener { handler() }
    }

    fun setErrorClickListener(handler: () -> Unit) {
        getChildAt(ERROR_VIEW_INDEX).setOnClickListener { handler() }
    }

    fun showError() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (ERROR_VIEW_INDEX == i) child.visibility = View.VISIBLE
            else child.visibility = View.GONE
        }
    }

    fun showLoading() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (LOADING_VIEW_INDEX == i) child.visibility = View.VISIBLE
            else child.visibility = View.GONE
        }
    }

    fun showEmpty() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (EMPTY_VIEW_INDEX == i) child.visibility = View.VISIBLE
            else child.visibility = View.GONE
        }
    }

    fun showContent() {
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (ERROR_VIEW_INDEX < i) child.visibility = View.VISIBLE
            else child.visibility = View.GONE
        }
    }
}