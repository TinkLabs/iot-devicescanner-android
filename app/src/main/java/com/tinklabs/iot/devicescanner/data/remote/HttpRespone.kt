package com.tinklabs.iot.devicescanner.data.remote

import com.google.gson.annotations.SerializedName

data class HttpRespone(
    @SerializedName("Code")
    val code: Int,
    @SerializedName("Msg")
    val message: String
)