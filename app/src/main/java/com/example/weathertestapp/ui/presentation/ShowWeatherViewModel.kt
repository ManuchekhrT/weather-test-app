package com.example.weathertestapp.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertestapp.data.model.DailyForecasts
import com.example.weathertestapp.data.network.NetworkResponse
import com.example.weathertestapp.data.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShowWeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _weatherList =
        MutableStateFlow<NetworkResponse<DailyForecasts>>(NetworkResponse.Loading())
    val weatherList: StateFlow<NetworkResponse<DailyForecasts>> = _weatherList

    fun fetchDailyForecastWeather(city: String) {
        viewModelScope.launch {
            weatherRepository.getDailyWeatherForecast(city)
                .catch { e ->
                    _weatherList.value = NetworkResponse.Error(e.toString())
                }
                .collect {
                    _weatherList.value = it
                }
        }
    }

}

