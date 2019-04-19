package com.tinklabs.iot.devicescanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_status")
data class StatusItem(
    @PrimaryKey
    val uid:Int,
    val status:String) {

    override fun hashCode(): Int {
        return uid
    }

    override fun toString(): String {
        return "{UID: $uid, STATUS: $status}"
    }
}