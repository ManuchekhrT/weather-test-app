package com.example.weathertestapp.data.network

sealed class NetworkResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Loading<T> : NetworkResponse<T>()
    class Success<T>(data: T?= null) : NetworkResponse<T>(data)
    class Error<T>(message: String, data: T? = null) :
        NetworkResponse<T>(data = data, message = message)
}