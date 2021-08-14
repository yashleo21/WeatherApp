package com.yash2108.weatherapp.di.modules

import android.app.Application
import androidx.room.Room
import com.yash2108.weatherapp.database.AppDatabase
import com.yash2108.weatherapp.database.dao.CurrentDao
import com.yash2108.weatherapp.database.dao.LocationDao
import com.yash2108.weatherapp.database.dao.RequestDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(context: Application): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "weather_database").build()

    @Singleton
    @Provides
    fun providesRequestDao(database: AppDatabase): RequestDao = database.requestDao()

    @Singleton
    @Provides
    fun providesCurrentDao(database: AppDatabase): CurrentDao = database.currentDao()

    @Singleton
    @Provides
    fun providesLocationDao(database: AppDatabase): LocationDao = database.locationDao()

}