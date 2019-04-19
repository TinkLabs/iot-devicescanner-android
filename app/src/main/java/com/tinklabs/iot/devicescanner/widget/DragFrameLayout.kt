package com.tinklabs.iot.devicescanner.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.customview.widget.ViewDragHelper

class DragFrameLayout: FrameLayout {
    private var mDragHelper: ViewDragHelper

    init {
        mDragHelper = ViewDragHelper.create(this, 1.0f, ViewDragCallback())
    }

    private inner class ViewDragCallback: ViewDragHelper.Callback() {

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            if (paddingLeft > left) return paddingLeft
            if (width - child.width < left) return width - child.width
            return left
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            if (paddingTop > top) return paddingTop
            if (height - child.height < top) return height - child.height
            return top
        }

        override fun onViewDragStateChanged(state: Int) {
            when(state) {
                ViewDragHelper.STATE_DRAGGING -> {

                }
                ViewDragHelper.STATE_IDLE -> {

                }
                ViewDragHelper.STATE_SETTLING -> {

                }
            }
            super.onViewDragStateChanged(state)
        }
    }

    constructor(context: Context): super(context)
    constructor(context: Context, attributes: AttributeSet): super(context, attributes)
    constructor(context: Context, attributes: AttributeSet, defStyle: Int): super(context, attributes, defStyle)

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when(ev?.action) {
            MotionEvent.ACTION_CANCEL -> {}
            MotionEvent.ACTION_DOWN -> {
                mDragHelper.cancel()
            }
        }
        return mDragHelper.shouldInterceptTouchEvent(ev!!)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        mDragHelper.processTouchEvent(event!!)
        return true
    }
}