package com.example.weathertestapp.data.network

import com.example.weathertestapp.data.model.DailyForecasts
import com.example.weathertestapp.data.model.LocalityResponse
import com.example.weathertestapp.utils.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {
    @GET("weather/1.0/report.json")
    suspend fun getDailyWeatherForecasts(
        @Query(Constants.PRODUCT) product: String = Constants.FORECAST_7DAYS_SIMPLE,
        @Query(Constants.APP_ID) appId: String = Constants.HERE_APP_ID_VALUE,
        @Query(Constants.APP_CODE) appCode: String = Constants.HERE_APP_CODE_VALUE,
        @Query(Constants.NAME) city: String,
    ) : Response<DailyForecasts>

    @GET
    suspend fun searchLocalities(
        @Url url: String
    ) : Response<LocalityResponse>
}