package com.tinklabs.iot.devicescanner.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tinklabs.iot.devicescanner.data.StatusItem

@Database(entities = [StatusItem::class], version = 1)
abstract class AppDataBase: RoomDatabase() {
    abstract fun statusDao(): StatusDao
}