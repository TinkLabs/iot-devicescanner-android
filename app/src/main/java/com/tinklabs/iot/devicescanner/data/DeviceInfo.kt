package com.tinklabs.iot.devicescanner.data

import java.util.*

data class DeviceInfo(var imei: String, var snCode: String) {
    val uid: String = UUID.randomUUID().toString().replace("-","")

    fun isValid() = !imei.isEmpty() && !imei.isEmpty()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false
        other as DeviceInfo
        if (imei != other.imei || snCode != other.snCode) {
            return false
        }
        return true
    }
}