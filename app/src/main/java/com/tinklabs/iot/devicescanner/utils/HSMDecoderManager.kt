package com.tinklabs.iot.devicescanner.utils

import android.content.Context
import android.graphics.Color
import androidx.preference.PreferenceManager
import com.honeywell.barcode.HSMDecoder
import com.honeywell.barcode.Symbology
import com.honeywell.license.ActivationManager
import com.honeywell.plugins.decode.DecodeResultListener
import com.tinklabs.iot.devicescanner.BuildConfig
import com.tinklabs.iot.devicescanner.R

class HSMDecoderManager constructor(private val context: Context) {
    private lateinit var hsmDecoder: HSMDecoder

    fun init() {
        ActivationManager.activate(context, BuildConfig.SWIFT_DECODER_LICENSE)

        hsmDecoder = HSMDecoder.getInstance(context)

        //set all decoder related settings
        resetUPCA()
        resetCODE128()
        resetCODE39()
        resetQR()
        resetEAN13()
        resetSound()

        hsmDecoder.enableFlashOnDecode(false)
        hsmDecoder.enableAimer(true)
        hsmDecoder.setAimerColor(Color.GREEN)
        hsmDecoder.setOverlayText(context.getString(R.string.scan_component_tip))
        hsmDecoder.setOverlayTextColor(Color.RED)
    }

    fun release() {
        HSMDecoder.disposeInstance()
    }

    fun addResultListener(listener: DecodeResultListener) {
        hsmDecoder.addResultListener(listener)
    }

    fun removeResultListener(listener: DecodeResultListener) {
        hsmDecoder.removeResultListener(listener)
    }

    fun resetSound() {
        hsmDecoder.enableSound(PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            context.getString(R.string.sp_key_enable_sound),
            context.resources.getBoolean(R.bool.sp_default_enable_sound)))
    }

    fun resetUPCA() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.sp_key_enable_UPCA),
                context.resources.getBoolean(R.bool.sp_default_enable_UPCA))) {
            hsmDecoder.enableSymbology(Symbology.UPCA)
        } else {
            hsmDecoder.disableSymbology(Symbology.UPCA)
        }
    }

    fun resetCODE128() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.sp_key_enable_CODE128),
                context.resources.getBoolean(R.bool.sp_default_enable_CODE128))) {
            hsmDecoder.enableSymbology(Symbology.CODE128)
        } else {
            hsmDecoder.disableSymbology(Symbology.CODE128)
        }
    }

    fun resetCODE39() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.sp_key_enable_CODE39),
                context.resources.getBoolean(R.bool.sp_default_enable_CODE39))) {
            hsmDecoder.enableSymbology(Symbology.CODE39)
        } else {
            hsmDecoder.disableSymbology(Symbology.CODE39)
        }
    }

    fun resetQR() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.sp_key_enable_QR),
                context.resources.getBoolean(R.bool.sp_default_enable_QR))) {
            hsmDecoder.enableSymbology(Symbology.QR)
        } else {
            hsmDecoder.disableSymbology(Symbology.QR)
        }
    }

    fun resetEAN13() {
        if (PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.sp_key_enable_EAN13),
                context.resources.getBoolean(R.bool.sp_default_enable_EAN13))) {
            hsmDecoder.enableSymbology(Symbology.EAN13)
        } else {
            hsmDecoder.disableSymbology(Symbology.EAN13)
        }
    }
}