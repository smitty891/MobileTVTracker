package com.tvtracker.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ImdbRetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private val BASE_URL = "https://movie-database-alternative.p.rapidapi.com"

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(ImdbInterceptor())
    }.build()

    val retrofitInstance : Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

}