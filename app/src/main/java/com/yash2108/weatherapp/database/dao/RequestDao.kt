package com.yash2108.weatherapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.yash2108.weatherapp.database.entitiy.Request

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Request)

    @Delete
    suspend fun deleteAll()
}