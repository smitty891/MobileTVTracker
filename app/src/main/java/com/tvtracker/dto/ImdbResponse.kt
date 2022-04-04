package com.tvtracker.dto

import com.google.gson.annotations.SerializedName

data class ImdbResponse(
    @SerializedName("Search") var results: ArrayList<MediaItem> = ArrayList<MediaItem>(),
    @SerializedName("totalResults") var totalResults: Int = 0,
    @SerializedName("Response") var success: Boolean = false
)
