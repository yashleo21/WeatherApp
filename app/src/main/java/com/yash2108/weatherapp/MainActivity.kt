package com.yash2108.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.yash2108.weatherapp.adapters.HomeAdapter
import com.yash2108.weatherapp.application.MyApplication
import com.yash2108.weatherapp.databinding.ActivityMainBinding
import com.yash2108.weatherapp.di.components.HomeActivityComponent
import com.yash2108.weatherapp.models.ResultUI
import com.yash2108.weatherapp.models.WeatherResponse
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private val viewModel by viewModels<HomeViewModel>()
    lateinit var activityComponent: HomeActivityComponent

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MyApplication).appComponent.homeActivityComponent().create()
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityComponent.inject(viewModel)
        initAdapter()
        initObservers()
        fetchInitialData()
        fetchData()
    }

    private fun initObservers() {
        viewModel.homeDataLiveData.observe(this, Observer {
            onHomeDataStateLoaded(state = it)
        })
    }

    private fun onHomeDataStateLoaded(state: ResultUI<List<WeatherResponse>>) {
        when (state) {
            is ResultUI.Loading -> {
                //Show progress
                binding.rlProgress.visibility = View.VISIBLE
            }

            is ResultUI.Error -> {
                //Show Error
                binding.rlProgress.visibility = View.GONE
            }

            is ResultUI.Success -> {
                //Show Success
                updateUI(state.data)
                updateAdapter(state.data)
                binding.rlProgress.visibility = View.GONE
                binding.llContent.visibility = View.VISIBLE
            }
        }
    }

    private fun fetchData() {
       viewModel.fetchWeatherInfo("noida")
    }

    private fun fetchInitialData() {
        viewModel.fetchInitialData()
    }

    private fun updateUI(data: List<WeatherResponse>) {
        data.elementAtOrNull(0)?.let {
            binding.tvTemperature.text = "${it.locationWithCurrentData?.current?.temperature}°C"
            binding.tvCity.text = it.locationWithCurrentData?.location?.name
        }
    }

    private fun updateAdapter(data: List<WeatherResponse>) {
        data?.elementAtOrNull(0)?.let {
            viewModel.prepareAdapterList(it)
            adapter.submitList(viewModel.adapterList.toList())
        }
    }

    private fun initAdapter() {
        binding.rvItems.adapter = adapter
    }

}