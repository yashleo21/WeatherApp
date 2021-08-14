package com.yash2108.weatherapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.yash2108.weatherapp.adapters.HomeAdapter
import com.yash2108.weatherapp.application.MyApplication
import com.yash2108.weatherapp.databinding.ActivityMainBinding
import com.yash2108.weatherapp.di.components.HomeActivityComponent
import com.yash2108.weatherapp.models.ResultUI
import com.yash2108.weatherapp.models.WeatherResponse
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val REQUEST_CODE_CHECK_SETTINGS = 111

    private val viewModel by viewModels<HomeViewModel>()
    lateinit var activityComponent: HomeActivityComponent

    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: HomeAdapter

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private val activityResultLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
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
                Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
                binding.layoutError.layoutError.visibility = View.VISIBLE
                checkLocationPermission()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        activityComponent =
            (application as MyApplication).appComponent.homeActivityComponent().create(this)
        activityComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activityComponent.inject(viewModel)
        initAdapter()
        initObservers()
        initListeners()
        getLastKnownLocation()
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
                binding.layoutError.layoutError.visibility = View.GONE
            }

            is ResultUI.Error -> {
                //Show Error
                binding.rlProgress.visibility = View.GONE
                binding.layoutError.layoutError.visibility = View.VISIBLE
            }

            is ResultUI.Success -> {
                //Show Success
                updateUI(state.data)
                updateAdapter(state.data)
                binding.layoutError.layoutError.visibility = View.GONE
                binding.rlProgress.visibility = View.GONE
                binding.llContent.visibility = View.VISIBLE
            }
        }
    }

    private fun initListeners() {
        binding.layoutError.btnRetry.setOnClickListener {
            binding.layoutError.layoutError.visibility = View.GONE
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        activityResultLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
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
            checkLocationPermission()
            return
        }
        fusedLocationClient.lastLocation?.addOnSuccessListener {
            if (it == null) {
                Log.d(TAG, "Null last known location, requesting new locaiton")
                requestNewLocation()
                return@addOnSuccessListener
            }
            Log.d(TAG, "Location object: ${it?.latitude}")
            fetchData(it)
        }
    }

    private fun fetchData(location: Location?) {
        if (location == null) {
            Toast.makeText(this, "Unable to receive last known location", Toast.LENGTH_LONG).show()
            return
        }
        viewModel.fetchWeatherInfo(if (location != null) "${location?.latitude},${location?.longitude}" else "noida")
    }

    private fun requestNewLocation() {
        val locationRequest = LocationRequest.create()
        locationRequest.interval = 60000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.locations?.elementAtOrNull(0)?.let {
                    Log.d(TAG, "New location received: $it")
                    fusedLocationClient.removeLocationUpdates(this)
                    fetchData(it)
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkLocationPermission()
            return
        }

        LocationServices
            .getSettingsClient(this)
            .checkLocationSettings(builder.build())
            .addOnSuccessListener {
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
            }
            .addOnFailureListener {
                if (it is ResolvableApiException) {
                    try {
                        it.startResolutionForResult(this, REQUEST_CODE_CHECK_SETTINGS)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_CODE_CHECK_SETTINGS == requestCode) {
            if (Activity.RESULT_OK == resultCode) {
                requestNewLocation()
            } else {
                binding.layoutError.layoutError.visibility = View.VISIBLE
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}