package com.yash2108.weatherapp.repository

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HomeRepository constructor(private val remoteDataSource: HomeDataSource, {

    private val TAG = HomeRepository::class.java.simpleName

    suspend fun getIssuesList(): Flow<ResultUI<List<HomeDataObject>>> {
        return flow {
            emit(ResultUI.loading())
            val result = remoteDataSource.getData()
            emit(ResultUI.success(result))
        }
    }
}