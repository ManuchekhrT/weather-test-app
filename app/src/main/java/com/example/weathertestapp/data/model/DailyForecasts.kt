package com.example.weathertestapp.data.model

import com.example.weathertestapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class DailyForecasts(
    @SerializedName(Constants.DAILY_FORECASTS_FIELD)
    val dailyForecasts: ForecastLocation
) {
    data class ForecastLocation(
        @SerializedName(Constants.FORECAST_LOCATION_FIELD)
        val forecast: Forecast
    ) {
        data class Forecast(
            @SerializedName(Constants.FORECAST_FIELD)
            val forecastWeathers: List<Weather>
        )
    }
}