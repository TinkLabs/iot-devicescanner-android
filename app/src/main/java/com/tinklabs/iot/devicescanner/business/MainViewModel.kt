package com.tinklabs.iot.devicescanner.business

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel()  {
    private var _isSignedIn: MutableLiveData<Boolean> = MutableLiveData()
    val isSignedIn: LiveData<Boolean>
        get() = _isSignedIn

    fun setSignInStatus(boolean: Boolean) {
        _isSignedIn.value = boolean
    }
}