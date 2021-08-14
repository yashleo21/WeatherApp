package com.yash2108.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
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

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var allPermissionsGranted = true
            permissions.entries.forEach {
                Log.d(TAG, "${it.key} = ${it.value}")
                if (it.value == false) {
                    allPermissionsGranted = false
                    return@forEach
                }
            }
            // Handle Permission granted/rejected
            if (allPermissionsGranted) {
                // Permission is granted
                Log.d(TAG, "Permission granted")
                getLastKnownLocation()
            } else {
                // Permission is denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent = (application as MyApplication).appComponent.homeActivityComponent().create(this)
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityComponent.inject(viewModel)
        initAdapter()
        initObservers()
       // fetchInitialData()
        checkLocationPermission()
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

    private fun checkLocationPermission() {
        activityResultLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
    }

    private fun getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "No permission")
            return
        }
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            Log.d(TAG, "Location object: ${it.latitude}")
            fetchData(it)
        }
    }

    private fun fetchData(location: Location?) {
       viewModel.fetchWeatherInfo(if (location != null)"${location?.latitude},${location?.longitude}" else "noida")
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