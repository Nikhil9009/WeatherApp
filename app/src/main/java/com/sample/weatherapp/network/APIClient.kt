package com.sample.weatherapp.network

import com.sample.weatherapp.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.schedulers.Schedulers
import java.util.concurrent.TimeUnit


object APIClient {

    /**
     * Retrofit network call configuration
     */
    fun getRetrofitAdapter(baseURL: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(getExtraInterceptorsBuilder().build())
            .build()

    private fun getExtraInterceptorsBuilder(): OkHttpClient.Builder {
        val clientBuilder = OkHttpClient.Builder()

        /**
         *  Connection pool configuration
         *      -> At a time it allows defined maximum connections only
         *      -> Connection alive duration also we can customize
         */
        clientBuilder.connectionPool(
            ConnectionPool(
                APIConstants.ClientConfig.MAX_IDEAL_CONNECTIONS,
                APIConstants.ClientConfig.KEEP_ALIVE_DURATION,
                TimeUnit.SECONDS
            )
        )

        /**
         * Auto connection and read time out for network call
         */
        clientBuilder.connectTimeout(APIConstants.ClientConfig.REQUEST_TIME_OUT, TimeUnit.MINUTES)
        clientBuilder.readTimeout(APIConstants.ClientConfig.REQUEST_TIME_OUT, TimeUnit.MINUTES)

        /**
         * Logging will be auto disable when build went for production
         *
         *  -> Logging enable when build in Debug mode
         *  -> Logging disable when build in production or release mode
         *
         */
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        clientBuilder.addInterceptor(logging)

        return clientBuilder
    }
}