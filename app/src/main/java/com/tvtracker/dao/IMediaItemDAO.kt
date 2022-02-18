package com.tvtracker.dao

import com.tvtracker.dto.ImdbResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IMediaItemDAO {

    @GET("/")
    fun searchIMDB(@Query("s") searchTxt: String, @Query("type") type: String, @Query("page") page: Int): Call<ImdbResponse>

}