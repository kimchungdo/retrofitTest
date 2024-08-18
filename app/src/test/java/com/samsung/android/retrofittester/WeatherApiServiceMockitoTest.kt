package com.samsung.android.retrofittester

import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import retrofit2.Call
import retrofit2.Response

class WeatherApiServiceMockitoTest {

    private lateinit var apiService: WeatherApiService
    private lateinit var mockCall: Call<WeatherResponse>

    @Before
    fun setUp() {
        apiService = mock(WeatherApiService::class.java)
        mockCall = mock(Call::class.java) as Call<WeatherResponse>
    }

    @Test
    fun getCurrentWeatherWithMockitoReturnsSuccess_Test() {

        //Mock 응답 설정하기

        val weatherResponse = WeatherResponse(
            name = "Seoul",
            main = Main(temp = 280.32f),
            weather = listOf(Weather(description = "clear sky"))
        )
        `when`(mockCall.execute()).thenReturn(Response.success(weatherResponse))
        `when`(apiService.getCurrentWeather("Seoul", "test_api_key")).thenReturn(mockCall)


        //API 호출
        val response = apiService.getCurrentWeather("Seoul", "test_api_key").execute()


        //응답 검증
        assertTrue(response.isSuccessful)
        assertNotNull(response.body())

        val responseBody = response.body()
        assertEquals("Seoul", responseBody?.name)
        assertEquals(280.32f, responseBody?.main?.temp)
        assertEquals("clear sky", responseBody?.weather?.get(0)?.description)
    }

    @Test
    fun `test getCurrentWeather with Mockito returns failure`() {
        // Mock 실패 응답 설정
        val response: Response<WeatherResponse> = Response.error(401, mock())
        `when`(mockCall.execute()).thenReturn(response)
        `when`(apiService.getCurrentWeather("Seoul", "invalid_api_key")).thenReturn(mockCall)

        // API 호출
        val result = apiService.getCurrentWeather("Seoul", "invalid_api_key").execute()

        // 실패 검증
        assertFalse(result.isSuccessful)
        assertEquals(401, result.code())
    }

}