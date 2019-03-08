package com.tinklabs.iot.devicescanner.data.remote

data class UploadModel(val account: String, val barcodes: List<BarCode>) {
    data class BarCode(val status: String, val imei: String, val sn: String)
}