package com.yash2108.weatherapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash2108.weatherapp.models.AdapterObject
import com.yash2108.weatherapp.models.ResultUI
import com.yash2108.weatherapp.models.WeatherResponse
import com.yash2108.weatherapp.repository.HomeRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel : ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    val adapterList = ArrayList<AdapterObject>()

    @Inject
    lateinit var repository: HomeRepository

    private var homeDataMutableLiveData: MutableLiveData<ResultUI<List<WeatherResponse>>> =
        MutableLiveData()
    val homeDataLiveData get(): LiveData<ResultUI<List<WeatherResponse>>> = homeDataMutableLiveData

    fun fetchWeatherInfo(query: String) = viewModelScope.launch {
        repository.getWeatherData(query)?.collect {
            homeDataMutableLiveData.postValue(it)
        }
    }

    fun fetchInitialData() = viewModelScope.launch {
        repository.getInitialData().collect {
            homeDataMutableLiveData.postValue(it)
        }
    }

    fun prepareAdapterList(data: WeatherResponse) {
        adapterList.clear()
        //
        adapterList.add(
            AdapterObject(
                "Wind Speed",
                data.locationWithCurrentData?.current?.wind_speed ?: ""
            )
        )
        adapterList.add(
            AdapterObject(
                "Pressure",
                data.locationWithCurrentData?.current?.pressure ?: ""
            )
        )
        adapterList.add(
            AdapterObject(
                "Precip",
                data.locationWithCurrentData?.current?.precip ?: ""
            )
        )
        adapterList.add(
            AdapterObject(
                "Cloud Cover",
                data.locationWithCurrentData?.current?.cloudcover ?: ""
            )
        )
    }

}