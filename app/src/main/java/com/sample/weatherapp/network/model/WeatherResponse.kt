package com.sample.weatherapp.network.model


import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("city") val city: City?,
    @SerializedName("cnt") val cnt: Int?,
    @SerializedName("cod") val cod: String?,
    @SerializedName("list") val list: List<ResponseData>?,
    @SerializedName("message") val message: Int?
)