package com.samsung.android.retrofittester

import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class AccuWeatherApiServiceTest {
    private lateinit var apiService: AccuWeatherApiService

    @Before
    fun setUp() {
        // Retrofit 인터페이스 Mocking
        apiService = mock()
    }
    @Test
    fun `test getLocationKey returns success`() {
        // 가짜 Location Key 응답 설정
        val locationResponse = listOf(
            LocationResponse(Key = "12345", LocalizedName = "Seoul", Country = Country(LocalizedName = "South Korea"))
        )

        // Mocking된 API가 성공적으로 Location Key를 반환하도록 설정
        `when`(apiService.getLocationKey(any(), any())).thenReturn(Single.just(locationResponse))

        // API 호출
        apiService.getLocationKey("Seoul", "test_api_key")
            .test()
            .assertNoErrors()
            .assertValue {
                it.isNotEmpty() && it[0].Key == "12345"
            }
    }

    @Test
    fun `test getCurrentConditions returns success`() {
        // 가짜 날씨 응답 설정
        val weatherResponse = listOf(
            CurrentConditionsResponse(
                WeatherText = "Clear",
                Temperature = Temperature(Metric = Metric(Value = 20.0f, Unit = "C"))
            )
        )

        // Mocking된 API가 성공적으로 날씨 데이터를 반환하도록 설정
        `when`(apiService.getCurrentConditions(any(), any())).thenReturn(Single.just(weatherResponse))

        // API 호출
        apiService.getCurrentConditions("12345", "test_api_key")
            .test()
            .assertNoErrors()
            .assertValue {
                it.isNotEmpty() && it[0].WeatherText == "Clear"
            }
    }

}