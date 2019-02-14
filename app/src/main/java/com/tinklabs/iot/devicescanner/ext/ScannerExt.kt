package com.tinklabs.iot.devicescanner.ext

import com.tinklabs.iot.devicescanner.data.DeviceInfo
import com.tinklabs.iot.devicescanner.data.remote.UploadModel

fun DeviceInfo.transform(status:String): UploadModel {
    return UploadModel(status, this.imei, this.snCode)
}