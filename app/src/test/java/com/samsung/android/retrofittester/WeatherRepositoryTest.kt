import com.samsung.android.retrofittester.AccuWeatherApiService
import com.samsung.android.retrofittester.Country
import com.samsung.android.retrofittester.CurrentConditionsResponse
import com.samsung.android.retrofittester.LocationResponse
import com.samsung.android.retrofittester.Metric
import com.samsung.android.retrofittester.Temperature
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.kotlin.any
import org.mockito.kotlin.mock

class WeatherRepositoryTest {

    private lateinit var apiService: AccuWeatherApiService
    private lateinit var repository: WeatherRepository

    @Before
    fun setUp() {
        apiService = mock()
        repository = WeatherRepository(apiService)
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
    }

    @After
    fun tearDown() {
        // 테스트 후 원래 스케줄러로 복원
        RxAndroidPlugins.reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `test fetchWeatherForCity returns weather successfully`() {
        // 가짜 Location Key 응답 설정
        val locationResponse = listOf(
            LocationResponse(Key = "12345", LocalizedName = "Seoul", Country = Country(LocalizedName = "South Korea"))
        )

        // 가짜 날씨 응답 설정
        val weatherResponse = listOf(
            CurrentConditionsResponse(
                WeatherText = "Clear",
                Temperature = Temperature(Metric = Metric(Value = 20.0f, Unit = "C"))
            )
        )

        // Location Key API가 성공적으로 Location Key를 반환하도록 설정
        `when`(apiService.getLocationKey(any(), any())).thenReturn(Single.just(locationResponse))

        // 날씨 API가 성공적으로 날씨 데이터를 반환하도록 설정
        `when`(apiService.getCurrentConditions(any(), any())).thenReturn(Single.just(weatherResponse))

        // 테스트 실행
        repository.fetchWeatherForCity("Seoul", "test_api_key")
            .test()
            .assertNoErrors()  // 에러가 없음을 확인
            .assertValue { weather ->
                // 올바른 날씨 정보를 반환하는지 확인
                weather.WeatherText == "Clear" && weather.Temperature.Metric.Value == 20.0f
            }
    }

//    @Test
//    fun `test fetchWeatherForCity returns error when location key not found`() {
//        // 가짜 Location Key 응답 설정 (빈 리스트)
//        val emptyLocationResponse = listOf<LocationResponse>()
//
//        // Location Key API가 빈 리스트를 반환하도록 설정
//        `when`(apiService.getLocationKey(any(), any())).thenReturn(Single.just(emptyLocationResponse))
//
//        // 테스트 실행
//        repository.fetchWeatherForCity("UnknownCity", "test_api_key")
//            .test()
//            .assertError { error ->
//                // 에러 메시지가 "Location Key not found" 인지 확인
//                error.message == "Location Key not found"
//            }
//    }
}