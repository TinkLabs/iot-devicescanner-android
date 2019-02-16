package com.tinklabs.iot.devicescanner.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tinklabs.iot.devicescanner.data.StatusItem



@Dao
interface StatusDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg status: StatusItem)

    @Query("SELECT * FROM tb_status")
    fun loadAllStatus(): Array<StatusItem>
}