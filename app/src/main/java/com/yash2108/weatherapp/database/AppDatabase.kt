package com.yash2108.weatherapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yash2108.weatherapp.database.dao.CurrentDao
import com.yash2108.weatherapp.database.dao.LocationDao
import com.yash2108.weatherapp.database.dao.RequestDao
import com.yash2108.weatherapp.database.entitiy.Current
import com.yash2108.weatherapp.database.entitiy.Location
import com.yash2108.weatherapp.database.entitiy.Request

@Database(entities = arrayOf(Request::class, Location::class, Current::class), version = 1, exportSchema = true)
abstract class AppDatabase: RoomDatabase() {

    abstract fun currentDao(): CurrentDao

    abstract fun locationDao(): LocationDao

    abstract fun requestDao(): RequestDao
}