package com.yash2108.weatherapp.models

import com.yash2108.openissuesreader.network.service.RetrofitAPI
import java.lang.Exception
import javax.inject.Inject

class RemoteDataSource @Inject constructor(val service: RetrofitAPI) {

    suspend fun getData(query: String): Result<WeatherContainer> {
        return try {
            val request = service.getCurrentWeather(key = "2af98395a52bd9d67a523d4c8c02eeeb", query = query)
            Result.success(request)
        } catch (e: Exception) {
            Result.error(e)
        }
    }
}