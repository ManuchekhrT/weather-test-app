package com.example.weathertestapp.data.network

import javax.inject.Inject

class ApiDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun getDailyWeatherForecasts(city: String) = apiService.getDailyWeatherForecasts(city = city)

    suspend fun searchLocalities(url: String) = apiService.searchLocalities(url)

}