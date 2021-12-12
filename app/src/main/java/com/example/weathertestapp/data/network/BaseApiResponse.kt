package com.example.weathertestapp.data.network

import retrofit2.Response

abstract class BaseApiResponse {

    suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): NetworkResponse<T> {
        loading<T>()
        try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                body?.let {
                    return NetworkResponse.Success(body)
                }
            }
            return error("${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> loading(): NetworkResponse<T> =
        NetworkResponse.Loading()

    private fun <T> error(errorMessage: String): NetworkResponse<T> =
        NetworkResponse.Error("Api call failed $errorMessage")
}