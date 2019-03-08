package com.tinklabs.iot.devicescanner.utils

import android.content.Context
import android.content.SharedPreferences

class EmailAccountManager(context: Context) {
    companion object {
        const val PREFS_FILENAME      = "google.account.prefs"
        const val LOGIN_ACCOUNT_EMAIL = "login_google_email"
    }

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

    var account: String
        get() = prefs.getString(LOGIN_ACCOUNT_EMAIL, "")!!
        set(value) = prefs.edit().putString(LOGIN_ACCOUNT_EMAIL, value).apply()
}