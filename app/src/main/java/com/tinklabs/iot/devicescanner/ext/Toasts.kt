package com.tinklabs.iot.devicescanner.ext

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment


inline fun Context.toast(message: CharSequence): Toast = Toast
    .makeText(this, message, Toast.LENGTH_SHORT)
    .apply {
        show()
    }

inline fun Activity.toast(message: String) = applicationContext.toast(message)

inline fun Fragment.toast(message: String) = activity?.toast(message)