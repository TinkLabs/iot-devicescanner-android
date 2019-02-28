package com.tinklabs.iot.devicescanner.business.index

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tinklabs.iot.devicescanner.http.HttpApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class IndexViewModel(
    private val httpApi: HttpApi
) : ViewModel() {

    companion object {
        const val STATUS_LOADING = 0
        const val STATUS_EMPTY = 1
        const val STATUS_ERROR = 2
        const val STATUS_COMPLETE = 3
    }

    private val compDisposable = CompositeDisposable()

    private var _status = MutableLiveData<Int>()
    val status: LiveData<Int>
        get() = _status

    private var _stateData = MutableLiveData<List<String>>()
    val stateData: LiveData<List<String>>
        get() = _stateData


    @SuppressLint("CheckResult")
    fun loadStatus() {
        compDisposable.add(
            httpApi.getStates("https://ca7s8egmxc.execute-api.ap-southeast-1.amazonaws.com/prod/device-status")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.status
                }
                .subscribeWith(object : DisposableObserver<List<String>>() {

                    override fun onStart() {
                        super.onStart()
                        _status.value = STATUS_LOADING
                    }

                    override fun onComplete() {
                    }

                    override fun onNext(status: List<String>) {
                        if (status.isEmpty()) {
                            _status.value = STATUS_EMPTY
                            _stateData.value = emptyList()
                        } else {
                            _stateData.value = status
                            _status.value = STATUS_COMPLETE
                        }
                    }

                    override fun onError(e: Throwable) {
                        Timber.e(e)
                        _status.value = STATUS_ERROR
                    }
                })
        )
    }

    fun onDestroyView() {
        compDisposable.clear()
    }
}