package com.example.weathertestapp.data.repository

import com.example.weathertestapp.data.model.DailyForecasts
import com.example.weathertestapp.data.model.LocalityResponse
import com.example.weathertestapp.data.network.ApiDataSource
import com.example.weathertestapp.data.network.BaseApiResponse
import com.example.weathertestapp.data.network.NetworkResponse
import com.example.weathertestapp.utils.Constants
import com.example.weathertestapp.utils.HereMapsUrls
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiDataSource: ApiDataSource
) : WeatherRepository, BaseApiResponse() {

    override suspend fun getDailyWeatherForecast(city: String): Flow<NetworkResponse<DailyForecasts>> {
        return flow {
            emit(
                safeApiCall {
                     apiDataSource.getDailyWeatherForecasts(city)
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun searchLocalities(query: String): Flow<NetworkResponse<LocalityResponse>> {
        val generatedUrl =
            HereMapsUrls.AUTOCOMPLETE_SEARCH_URL + "?apiKey=${Constants.HERE_API_KEY}" + "&q=${query}"
        return flow {
            emit(
                safeApiCall {
                    apiDataSource.searchLocalities(generatedUrl)
                }
            )
        }.flowOn(Dispatchers.IO)
    }
}