package com.tinklabs.iot.devicescanner.ext

import com.tinklabs.iot.devicescanner.data.DeviceInfo
import com.tinklabs.iot.devicescanner.data.remote.UploadModel

fun DeviceInfo.transform(status:String): UploadModel.BarCode {
    return UploadModel.BarCode(status, this.imei, this.snCode)
}