package com.yash2108.weatherapp.repository

import android.util.Log
import com.yash2108.openissuesreader.ui.di.scopes.ActivityScoped
import com.yash2108.weatherapp.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ActivityScoped
class HomeRepository @Inject constructor(
    private val local: LocalDataSource,
    private val remote: RemoteDataSource
) {

    private val TAG = HomeRepository::class.java.simpleName

    suspend fun getWeatherData(query: String): Flow<ResultUI<List<WeatherResponse>>> = flow {

        emit(ResultUI.loading())

        val response = remote.getData(query)
        when (response) {
            is Result.Success -> {
                Log.d(TAG, "Success, inserting")
                local.insertNewData(response.data)
                emit(ResultUI.success(local.getInitialData()))
            }

            is Result.Error -> {
                Log.d(TAG, "Failure, flowing")
                emit(ResultUI.error(response.error))
            }
        }
    }

    suspend fun getInitialData(): Flow<ResultUI<List<WeatherResponse>>> = flow {
        emit(ResultUI.success(local.getInitialData()))
    }
}