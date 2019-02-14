package com.tinklabs.iot.devicescanner.http

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class BaseApiClient {
    private var mRetrofit: Retrofit? = null
    private var mClientBuilder: OkHttpClient.Builder? = null

    init {
        init()
    }

    private fun init() {
        val loggerInterceptor =
            HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.tag("HttpMessage").d(message) })
        loggerInterceptor.level = HttpLoggingInterceptor.Level.BODY

        mClientBuilder = OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .addInterceptor(loggerInterceptor)
            .connectTimeout(5 * 1000, TimeUnit.MILLISECONDS)

        mRetrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(mClientBuilder!!.build())
            .build()
    }

    companion object {
        const val BASE_URL = "http://rom.handy.travel/"
    }

    fun <T> createService(clz: Class<T>): T {
        return mRetrofit!!.create(clz)
    }
}