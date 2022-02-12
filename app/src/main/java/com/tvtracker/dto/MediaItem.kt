package com.tvtracker.dto

import com.google.gson.annotations.SerializedName

data class MediaItem(@SerializedName("imdbID")      var id: String = "",
                     @SerializedName("Title")       var title: String = "",
                     @SerializedName("Year")        var year: String = "",
                     @SerializedName("Type")        var type: String = "",
                     @SerializedName("Poster")      var imageUrl: String = "",
                     @SerializedName("platform")    var platform: String = "",
                     @SerializedName("description") var description: String = "",
                     @SerializedName("watched")     var watched: Boolean = false)
