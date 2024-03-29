package com.tinklabs.iot.devicescanner.business.batchscan

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.honeywell.barcode.HSMDecodeResult
import com.honeywell.plugins.decode.DecodeResultListener
import com.tinklabs.iot.devicescanner.business.batchscan.BatchScanFragment.Companion.IMEI_LENGTH
import com.tinklabs.iot.devicescanner.data.DeviceInfo
import com.tinklabs.iot.devicescanner.data.remote.HttpResponse
import com.tinklabs.iot.devicescanner.data.remote.UploadModel
import com.tinklabs.iot.devicescanner.ext.toast
import com.tinklabs.iot.devicescanner.ext.transform
import com.tinklabs.iot.devicescanner.http.HttpApi
import com.tinklabs.iot.devicescanner.utils.EmailAccountManager
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import com.tinklabs.iot.devicescanner.widget.LoadingDialogFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class BatchScanViewModel constructor(
    private val context: Context,
    private val httpApi: HttpApi
) : ViewModel(), DecodeResultListener {

    private val hsmDecoderManager = HSMDecoderManager(context)
    private val accountManager = EmailAccountManager(context)

    private val compDisposable = CompositeDisposable()
    private val loadingDialog: LoadingDialogFactory = LoadingDialogFactory(context)

    private val _deviceInfo = MutableLiveData<DeviceInfo>()
    val deviceInfo: LiveData<DeviceInfo>
        get() = _deviceInfo

    private val _valid = MutableLiveData<Boolean>()
    val valid: LiveData<Boolean>
        get() = _valid

    private lateinit var _items: MutableLiveData<MutableList<DeviceInfo>>
    val items: LiveData<MutableList<DeviceInfo>>
        get() {
            if (!::_items.isInitialized) {
                _items = MutableLiveData()
                loadItems()
            }
            return _items
        }

    private fun loadItems() {
        _items.value = mutableListOf()
    }

    fun removeItem(deviceInfo: DeviceInfo) {
        if (_items.value?.contains(deviceInfo)!!) {
            _items.value!!.remove(deviceInfo)
        }
        _items.value = _items.value
    }

    private fun resetValue() {
        _deviceInfo.value = DeviceInfo("", "")
    }

    private fun addItem(imei: String, snCode: String) {
        var had = false
        for (item in _items.value!!) {
            if (imei == item.imei) {
                had = true
                break
            }
        }
        if (!had) {
            _items.value?.add(DeviceInfo(imei, snCode))
            _items.value = _items.value
        }

    }

    fun upload(status: String, onSuccess: () -> Unit) {
        val barcodes = mutableListOf<UploadModel.BarCode>()
        _items.value?.forEach {
            barcodes.add(it.transform(status = status))
        }
        val account = accountManager.account
        compDisposable.add(
            httpApi.uploadDeviceInfo(UploadModel(account = account, barcodes = barcodes))
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<HttpResponse>() {

                    override fun onStart() {
                        super.onStart()
                        loadingDialog.show()
                    }

                    override fun onComplete() {
                        loadingDialog.dismiss()
                        dispose()
                    }

                    override fun onNext(result: HttpResponse) {
                        loadingDialog.dismiss()
                        Timber.d(result.message)
                        if (result.code == 0) {
                            // upload success will update UI
                            context.toast("Upload successful!!!")
                            loadItems()
                            resetValue()
                            onSuccess()
                        } else {
                            // upload failed tips error message
                            context.toast("Upload failed!!!")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        loadingDialog.dismiss()
                        context.toast(e.message ?: "Http request error")
                    }
                })
        )
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

    override fun onHSMDecodeResult(results: Array<out HSMDecodeResult>?) {
        resetValue()
        if (results != null && results.isNotEmpty()) {
            if (results.size < 2) return

            val barcodeData0 = results[0].barcodeData
            if (barcodeData0.length == IMEI_LENGTH) {
                _deviceInfo.value?.imei = barcodeData0
            } else {
                _deviceInfo.value?.snCode = barcodeData0
            }

            val barcodeData1 = results[1].barcodeData
            if (barcodeData1.length == IMEI_LENGTH) {
                _deviceInfo.value?.imei = barcodeData1
            } else {
                _deviceInfo.value?.snCode = barcodeData1
            }

            _valid.value = _deviceInfo.value?.isValid()
            if (_valid.value!!) addItem(_deviceInfo.value?.imei!!, _deviceInfo.value?.snCode!!)
        }
    }
}