package com.sample.weatherapp.network.error

object ErrorHandling {
    fun getNoNetworkError(t: Throwable? = null): Error = Error("No Internet connection. Make sure that Wi-Fi or cellular mobile data is turned on, then try again.", t)
}