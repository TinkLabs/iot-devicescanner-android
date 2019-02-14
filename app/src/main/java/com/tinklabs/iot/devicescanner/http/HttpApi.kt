package com.tinklabs.iot.devicescanner.http

import com.tinklabs.iot.devicescanner.data.remote.HttpRespone
import com.tinklabs.iot.devicescanner.data.remote.UploadModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface HttpApi {
    @POST("barcode")
    fun uploadDeviceInfo(@Body body: List<UploadModel>): Observable<HttpRespone>
}