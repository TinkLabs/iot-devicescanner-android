package com.tinklabs.iot.devicescanner.business.singlescan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honeywell.barcode.HSMDecodeResult
import com.honeywell.plugins.decode.DecodeResultListener
import com.tinklabs.iot.devicescanner.data.DeviceInfo
import com.tinklabs.iot.devicescanner.data.remote.HttpResponse
import com.tinklabs.iot.devicescanner.ext.toast
import com.tinklabs.iot.devicescanner.ext.transform
import com.tinklabs.iot.devicescanner.http.HttpApi
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class SingleScanViewModel constructor(
    private val context: Context,
    private val hsmDecoderManager: HSMDecoderManager,
    private val httpApi: HttpApi
) : ViewModel(), DecodeResultListener {

    private val compDisposable = CompositeDisposable()

    companion object {
        const val IMEI_LENGTH = 15
    }

    private val _deviceInfo = MutableLiveData<DeviceInfo>()
    val deviceInfo: LiveData<DeviceInfo>
        get() = _deviceInfo

    private val _valid = MutableLiveData<Boolean>()
    val valid: LiveData<Boolean>
        get() = _valid

    init {
        resetValue()
    }

    private fun resetValue() {
        _deviceInfo.value = DeviceInfo("", "")
    }

    fun onResume() {
        hsmDecoderManager.addResultListener(this)
    }

    fun onPause() {
        hsmDecoderManager.removeResultListener(this)
    }

    fun onDestroyView() {
        compDisposable.clear()
    }

    fun upload(status: String) {
        compDisposable.add(httpApi.uploadDeviceInfo(listOf(deviceInfo.value?.transform(status = status)!!))
            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<HttpResponse>() {
                override fun onComplete() {
                    dispose()
                }

                override fun onNext(result: HttpResponse) {
                    Timber.d(result.message)

                    if (result.code == 0) {
                        // upload success will update UI. if need handle add call back
                    } else {
                        // upload failed tips error message
                    }
                }

                override fun onError(e: Throwable) {
                    Timber.e(e)
                    context.toast(e.message ?: "Http request error")
                }
            })
        )
    }

    override fun onHSMDecodeResult(results: Array<out HSMDecodeResult>?) {
        resetValue()

        if (null != results && results.isNotEmpty()) {
            if (results.size < 2) return

            val barcodeData0 = results[0].barcodeData
            if (barcodeData0.length == SingleScanViewModel.IMEI_LENGTH) {
                _deviceInfo.value?.imei = barcodeData0
            } else {
                _deviceInfo.value?.snCode = barcodeData0
            }
            val barcodeData1 = results[1].barcodeData
            if (barcodeData1.length == SingleScanViewModel.IMEI_LENGTH) {
                _deviceInfo.value?.imei = barcodeData1
            } else {
                _deviceInfo.value?.snCode = barcodeData1
            }

            _valid.value = _deviceInfo.value?.isValid()
        }
    }
}