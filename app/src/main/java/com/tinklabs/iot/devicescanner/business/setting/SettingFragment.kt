package com.tinklabs.iot.devicescanner.business.setting

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tinklabs.iot.devicescanner.BuildConfig
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import org.koin.android.ext.android.inject

class SettingFragment:PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private val ADDRESS = arrayOf("ivo.wu@tinklabs.com")
        private val CC = arrayOf("kurt.huang@tinklabs.com")
    }

    private val hsmDecoderManager:HSMDecoderManager by inject()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.setting, rootKey)

        preferenceManager.findPreference(getString(R.string.sp_key_feedback)).setOnPreferenceClickListener {
            composeEmail(ADDRESS, CC, "Feedback issue from ${getString(context!!.applicationInfo.labelRes)}")
            true
        }

        preferenceManager.findPreference(getString(R.string.sp_key_logout))
            .setOnPreferenceClickListener {
                logout()
                true
            }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            getString(R.string.sp_key_enable_sound) -> hsmDecoderManager.resetSound()
            getString(R.string.sp_key_enable_CODE128) -> hsmDecoderManager.resetCODE128()
            getString(R.string.sp_key_enable_CODE39) -> hsmDecoderManager.resetCODE39()
            getString(R.string.sp_key_enable_EAN13) -> hsmDecoderManager.resetEAN13()
            getString(R.string.sp_key_enable_QR) -> hsmDecoderManager.resetQR()
            getString(R.string.sp_key_enable_UPCA) -> hsmDecoderManager.resetUPCA()
        }
    }

    private fun logout() {
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOptions)
        mGoogleSignInClient.signOut()
        activity!!.finish()
    }

    private fun composeEmail(addresses: Array<String>, cc: Array<String>, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:") // only email apps should handle this
            putExtra(Intent.EXTRA_EMAIL, addresses)
            putExtra(Intent.EXTRA_CC, cc)
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        if (intent.resolveActivity(context!!.packageManager) != null) {
            startActivity(intent)
        }
    }
}