package com.example.weathertestapp.ui.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathertestapp.data.model.LocalityResponse
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
class SelectCityViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _city = MutableStateFlow("")
    val city: StateFlow<String> = _city

    private val _searchLocalities = MutableStateFlow<NetworkResponse<LocalityResponse>>(
        NetworkResponse.Loading()
    )
    val searchLocalities: StateFlow<NetworkResponse<LocalityResponse>> = _searchLocalities

    fun setCity(city: String) {
        _city.value = city
    }

    fun doSearchLocalities(q: String) {
        viewModelScope.launch {
            weatherRepository.searchLocalities(q)
                .catch { e ->
                    _searchLocalities.value = NetworkResponse.Error(e.toString())
                }
                .collect {
                    _searchLocalities.value = it
                }
        }
    }
}