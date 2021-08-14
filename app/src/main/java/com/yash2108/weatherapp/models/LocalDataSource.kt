package com.yash2108.weatherapp.models

import androidx.lifecycle.LiveData
import com.yash2108.openissuesreader.ui.di.scopes.ActivityScoped
import com.yash2108.weatherapp.database.dao.CurrentDao
import com.yash2108.weatherapp.database.dao.LocationDao
import com.yash2108.weatherapp.database.dao.RequestDao
import com.yash2108.weatherapp.database.entitiy.Current
import com.yash2108.weatherapp.database.entitiy.Location
import com.yash2108.weatherapp.database.entitiy.Request
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityScoped
class LocalDataSource @Inject constructor(val requestDao: RequestDao,
                                          val locationDao: LocationDao,
                                          val currentDao: CurrentDao) {

    fun getData(): LiveData<List<WeatherResponse>> = requestDao.getData()


    suspend fun insertNewData(data: WeatherContainer) {
        requestDao.deleteAll()
        val request = Request(type = data.request?.type, query = data.request?.query ?: "", data.request?.language, data.request?.unit)
        val location = Location(query = data.request?.query ?: "", data.location?.name, data.location?.country)
        val current = Current(query = data.request?.query ?: "", weather_code = data.current?.weather_code,
            observation_time = data.current?.observation_time, temperature = data.current?.temperature, wind_speed = data.current?.wind_speed, wind_degree = data.current?.wind_degree, wind_dir = data.current?.wind_dir, pressure = data.current?.pressure, precip = data.current?.precip, humidity = data.current?.humidity, cloudcover = data.current?.cloudcover, feelslike = data.current?.feelslike, uv_index = data.current?.uv_index, visibility = data.current?.visibility, is_day = data.current?.is_day)

        requestDao.insert(request)
        locationDao.insert(location)
        currentDao.insert(current)
    }
}