package com.tinklabs.iot.devicescanner.di

import com.tinklabs.iot.devicescanner.business.batchscan.BatchScanViewModel
import com.tinklabs.iot.devicescanner.business.singlescan.SingleScanViewModel
import com.tinklabs.iot.devicescanner.http.BaseApiClient
import com.tinklabs.iot.devicescanner.http.HttpApi
import com.tinklabs.iot.devicescanner.utils.HSMDecoderManager
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val appModule: Module = module {
    viewModel {
        SingleScanViewModel(get(), get())
    }

    viewModel {
        BatchScanViewModel(get(), get())
    }

    single {
        HSMDecoderManager(androidApplication())
    }

    single {
        BaseApiClient().createService(HttpApi::class.java)
    }
}