package com.yash2108.weatherapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import com.yash2108.weatherapp.database.entitiy.Location

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(data: Location)
}