package com.yash2108.weatherapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.yash2108.weatherapp.database.Constants
import com.yash2108.weatherapp.database.entitiy.Request
import com.yash2108.weatherapp.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Request)

    @Query("DELETE FROM ${Constants.WeatherRequestTable}")
    suspend fun deleteAll()

    @Transaction
    @Query("SELECT * from ${Constants.WeatherRequestTable}")
    fun getData(): LiveData<List<WeatherResponse>>
}