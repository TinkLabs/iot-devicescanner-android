package com.tinklabs.iot.devicescanner.utils

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup

class DragViewUtil {
    companion object {
        fun <T : ViewGroup.MarginLayoutParams> drag(
            view: View, rootView: View) {
            val layoutParams: T = view.layoutParams as T
            view.setOnTouchListener(
                TouchListener(
                    layoutParams = layoutParams,
                    rootView = rootView
                )
            )
        }
    }

    private class TouchListener(
        private val rootView: View,
        private var layoutParams: ViewGroup.MarginLayoutParams
    ) : View.OnTouchListener {
        private var lastX: Float = 0.0f
        private var lastY: Float = 0.0f
        private var firstMove = true

        override fun onTouch(view: View?, event: MotionEvent?): Boolean {
            view?.let {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        lastX = event.rawX
                        lastY = event.rawY
                    }
                    MotionEvent.ACTION_MOVE -> {
                        val dx = event.rawX.minus(lastX)
                        val dy = event.rawY.minus(lastY)
                        var left = if (firstMove) rootView.width.minus(view.width).toFloat()
                        else layoutParams.leftMargin.plus(dx)

                        var top = layoutParams.topMargin.plus(dy)
                        var bottom = rootView.height.minus(top).minus(view.height)
                        var right = if (firstMove) 0.0f else rootView.width.minus(left).minus(view.width)
                        firstMove = false
                        if (left < 0) {
                            left = 0.0f
                            right = rootView.width.minus(view.width).toFloat()
                        }
                        if (top < 0) {
                            top = 0.0f
                            bottom = rootView.height.minus(view.height).toFloat()
                        }
                        if (right <= 0) {
                            right = 0.0f
                            left = rootView.width.minus(view.width).toFloat()
                        }
                        if (bottom < 0) {
                            bottom = 0.0f
                            top = rootView.height.minus(view.height).toFloat()
                        }
                        layoutParams.leftMargin = left.toInt()
                        layoutParams.topMargin = top.toInt()
                        layoutParams.bottomMargin = bottom.toInt()
                        layoutParams.rightMargin = right.toInt()
                        view.layoutParams = layoutParams

                        lastX = event.rawX
                        lastY = event.rawY
                        view.postInvalidate()
                    }
                }
            }
            return true
        }
    }
}