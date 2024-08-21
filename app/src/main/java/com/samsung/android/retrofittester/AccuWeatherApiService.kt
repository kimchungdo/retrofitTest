package com.samsung.android.retrofittester

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AccuWeatherApiService {
    // 도시 이름으로 Location Key 검색
    @GET("locations/v1/cities/search")
    fun getLocationKey(
        @Query("q") cityName: String,
        @Query("apikey") apiKey: String
    ): Single<List<LocationResponse>>

    // 현재 날씨 정보 가져오기
    @GET("currentconditions/v1/{locationKey}")
    fun getCurrentConditions(
        @Path("locationKey") locationKey: String,
        @Query("apikey") apiKey: String
    ): Single<List<CurrentConditionsResponse>>
}