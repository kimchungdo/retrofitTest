import com.samsung.android.retrofittester.AccuWeatherApiService
import com.samsung.android.retrofittester.CurrentConditionsResponse
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepository(private val apiService: AccuWeatherApiService) {

    fun fetchWeatherForCity(cityName: String, apiKey: String): Single<CurrentConditionsResponse> {
        return apiService.getLocationKey(cityName, apiKey)
            .subscribeOn(Schedulers.io())
            .flatMap { locationResponse ->
                if (locationResponse.isNotEmpty()) {
                    val locationKey = locationResponse[0].Key
                    apiService.getCurrentConditions(locationKey, apiKey)
                        .subscribeOn(Schedulers.io())
                        .map { weatherResponse ->
                            weatherResponse[0]
                        }
                } else {
                    Single.error(Throwable("Location Key not found"))
                }
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
}