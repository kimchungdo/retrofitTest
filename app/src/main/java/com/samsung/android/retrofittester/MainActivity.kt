package com.samsung.android.retrofittester

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private val apiKey = "EDhYI38CkHdZ0296ZjGO10iMgGjZQDar"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityName = "Seoul"
        val apiService = RetrofitInstance.retrofit.create(AccuWeatherApiService::class.java)

        // Location Key 검색
        apiService.getLocationKey(cityName, apiKey)
            .subscribeOn(Schedulers.io())  // I/O 스레드에서 실행
            .observeOn(AndroidSchedulers.mainThread())  // 메인 스레드에서 결과 처리
            .subscribe(
                { locationResponse ->
                    if (locationResponse.isNotEmpty()) {
                        val locationKey = locationResponse[0].Key
                        Log.d("MainActivity", "Location Key: $locationKey")
                        // Location Key로 현재 날씨 검색
                        getCurrentWeather(locationKey)
                    }
                },
                { error ->
                    Log.e("MainActivity", "Failed to get location key: ${error.message}")
                }
            )
    }

    // Location Key로 현재 날씨 검색
    private fun getCurrentWeather(locationKey: String) {
        val apiService = RetrofitInstance.retrofit.create(AccuWeatherApiService::class.java)

        apiService.getCurrentConditions(locationKey, apiKey)
            .subscribeOn(Schedulers.io())  // I/O 스레드에서 실행
            .observeOn(AndroidSchedulers.mainThread())  // 메인 스레드에서 결과 처리
            .subscribe(
                { weatherResponse ->
                    if (weatherResponse.isNotEmpty()) {
                        val currentWeather = weatherResponse[0]
                        Log.d("MainActivity", "Weather: ${currentWeather.WeatherText}, Temp: ${currentWeather.Temperature.Metric.Value}${currentWeather.Temperature.Metric.Unit}")
                    }
                },
                { error ->
                    Log.e("MainActivity", "Failed to get weather data: ${error.message}")
                }
            )
    }
}