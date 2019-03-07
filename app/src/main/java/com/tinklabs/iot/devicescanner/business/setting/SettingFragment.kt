package com.tinklabs.iot.devicescanner.business.setting

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.tinklabs.iot.devicescanner.R
import com.tinklabs.iot.devicescanner.ext.toast
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    companion object {
        private val ADDRESS = arrayOf("ivo.wu@tinklabs.com")
        private val CC = arrayOf("kurt.huang@tinklabs.com")
    }

    private val hsmDecoderManager: HSMDecoderManager by inject { parametersOf(context!!) }

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

    private val googleSignInOptions: GoogleSignInOptions by inject()
    private fun logout() {
        val mGoogleSignInClient = GoogleSignIn.getClient(context!!, googleSignInOptions)
        mGoogleSignInClient.signOut()
            .addOnCompleteListener {
                // sign out success exit app
                if (it.isSuccessful) {
                    activity!!.finish()
                }
                // sign out failed tips
                else toast("Sign out failed")
            }
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