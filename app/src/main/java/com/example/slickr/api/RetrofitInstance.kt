package com.example.slickr.api

import com.example.slickr.util.Constants.Companion.BASE_URL
import com.example.slickr.util.Constants.Companion.CONNECTION_TIMEOUT_MS
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance  {


    val client: APIRequests by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(
                        GsonConverterFactory.create()
                )
                .client(
                        OkHttpClient.Builder().connectTimeout(
                                CONNECTION_TIMEOUT_MS,
                                TimeUnit.SECONDS
                        ).addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BASIC
                        }).build()
                )
                .build()
                .create(APIRequests::class.java)
    }
}
