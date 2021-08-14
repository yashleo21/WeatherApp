package com.yash2108.weatherapp.repository

import android.util.Log
import com.yash2108.weatherapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.sql.DataSource

class HomeRepository @Inject constructor(private val local: LocalDataSource, private val remote: RemoteDataSource) {

    private val TAG = HomeRepository::class.java.simpleName

    suspend fun getWeatherData(query: String): Flow<Result<List<WeatherResponse>>> = flow {

        val response = remote.getData(query)
        when (response) {
            is Result.Success -> {
                Log.d(TAG, "Success, inserting")
                local.insertNewData(response.data)
            }

            is Result.Error -> {
                Log.d(TAG, "Failure, flowing")
                emit(Result.error(response.error))
            }
        }
    }

    fun getDatabaseSource() =  local.getData()
}