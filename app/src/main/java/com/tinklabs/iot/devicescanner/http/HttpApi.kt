package com.tinklabs.iot.devicescanner.http

import com.tinklabs.iot.devicescanner.data.remote.HttpResponse
import com.tinklabs.iot.devicescanner.data.remote.StateResponse
import com.tinklabs.iot.devicescanner.data.remote.UploadModel
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Url

interface HttpApi {
    @POST("barcode")
    fun uploadDeviceInfo(@Body body: UploadModel): Observable<HttpResponse>

    @GET("")
    fun getStates(@Url url: String): Observable<StateResponse>

}