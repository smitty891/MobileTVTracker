package com.tvtracker.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {

    private var retrofit: Retrofit? = null
    private val IMDB_BASE_URL = "https://movie-database-imdb-alternative.p.rapidapi.com"

    private val imdbClient = OkHttpClient.Builder().apply {
        addInterceptor(ImdbInterceptor())
    }.build()

    val imdbRetrofitInstance : Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(IMDB_BASE_URL)
                    .client(imdbClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit
        }

}