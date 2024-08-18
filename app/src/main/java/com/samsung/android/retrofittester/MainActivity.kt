package com.samsung.android.retrofittester

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiKey = "3fbe7c1dc428236fab320bb69023de9f"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService = RetrofitInstance.retrofit.create(WeatherApiService::class.java)
        val call = apiService.getCurrentWeather("Seoul", apiKey)

        call.enqueue(object: Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    val weatherResponse = response.body()
                    Log.d("MainActivity", "Weather: ${weatherResponse?.weather?.get(0)?.description}, Temp: ${weatherResponse?.main?.temp}")
                } else {
                    Log.e("MainActivity", "Failed to get weather data: ${response.code()}, ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                Log.e("MainActivity", "Error: ${t.message}")
            }

        })
    }
}