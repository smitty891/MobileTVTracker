package com.tvtracker.api

import com.tvtracker.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ImdbInterceptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val xrapidapikey = "4ccbc10367msh3a0aae042eaac2ep10144bjsn5bf582dc9c1c"
        val request = chain.request()
            .newBuilder()
            .addHeader("x-rapidapi-key", xrapidapikey)
            .build()
        return chain.proceed(request)
    }
}