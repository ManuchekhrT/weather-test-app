package com.example.weathertestapp.data.model

import com.example.weathertestapp.utils.Constants
import com.google.gson.annotations.SerializedName

data class Weather(
    @SerializedName(Constants.DESCRIPTION)
    val description: String?,
    @SerializedName(Constants.SKY_DESC)
    val skyDesc: String?,
    @SerializedName(Constants.TEMP_DESC)
    val tempDesc: String?,
    @SerializedName(Constants.HIGH_TEMP)
    val highTemp: String?,
    @SerializedName(Constants.LOW_TEMP)
    val lowTemp: String?,
    @SerializedName(Constants.WEEKDAY)
    val weekday: String?,
    @SerializedName(Constants.ICON_NAME)
    val iconName: String?,
    @SerializedName(Constants.ICON_LINK)
    val iconLink: String?,
    @SerializedName(Constants.UTC_TIME)
    val utcTime: String?,
)