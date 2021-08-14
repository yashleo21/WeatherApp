package com.yash2108.weatherapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash2108.weatherapp.models.Result
import com.yash2108.weatherapp.models.ResultUI
import com.yash2108.weatherapp.repository.HomeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel: ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    @Inject
    lateinit var repository: HomeRepository

    fun getDatabaseSource() = repository.getDatabaseSource()

    fun fetchWeatherInfo(query: String) = viewModelScope.launch {
        repository.getWeatherData("noida")?.collect {
            when (it) {
                is ResultUI.Success -> {
                    Log.d(TAG, "Success flow should not call: ${it.data}")
                }

                is ResultUI.Error -> {
                    Log.d(TAG, "Failure: Flow can call ${it.error}")
                }

                is ResultUI.Loading -> {
                    Log.d(TAG, "Loading: Yep, loading")
                }
            }
        }
    }
}