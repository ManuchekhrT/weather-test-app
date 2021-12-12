package com.example.weathertestapp.data.model

import com.example.weathertestapp.utils.Constants
import com.google.gson.annotations.SerializedName


data class LocalityResponse(
    @SerializedName(Constants.ITEMS)
    val items: List<LocalityItem>
) {
    data class LocalityItem(
        @SerializedName(Constants.ADDRESS)
        val address: Locality
    )
}

data class Locality(
    @SerializedName(Constants.COUNTRY_NAME)
    val countryName: String?,
    @SerializedName(Constants.COUNTRY_CODE)
    val countryCode: String?,
    @SerializedName(Constants.CITY)
    val city: String?
)