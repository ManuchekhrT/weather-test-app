package com.example.weathertestapp.data.repository

import com.example.weathertestapp.data.model.DailyForecasts
import com.example.weathertestapp.data.model.LocalityResponse
import com.example.weathertestapp.data.network.NetworkResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getDailyWeatherForecast(city: String): Flow<NetworkResponse<DailyForecasts>>
    suspend fun searchLocalities(query: String): Flow<NetworkResponse<LocalityResponse>>
}