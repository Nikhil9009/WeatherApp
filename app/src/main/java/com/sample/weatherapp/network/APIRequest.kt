package com.sample.weatherapp.network

import com.sample.weatherapp.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.QueryMap
import rx.Observable

interface APIRequest {

    @GET("data/2.5/forecast")
    fun getCityWeather(@QueryMap map: Map<String, String>): Observable<WeatherResponse>

}