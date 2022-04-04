package com.tvtracker.dto

import com.google.gson.annotations.SerializedName

data class MediaItem(@SerializedName("mediaID")     var mediaId: String = "",
                     @SerializedName("imdbID")      var imdbId: String = "",
                     @SerializedName("imdbRating")  var imdbRating: String = "",
                     @SerializedName("Title")       var title: String = "",
                     @SerializedName("Year")        var year: String = "",
                     @SerializedName("Released")    var releaseDate: String = "",
                     @SerializedName("Rated")       var rated: String = "",
                     @SerializedName("Genre")       var genre: String = "",
                     @SerializedName("Poster")      var imageUrl: String = "",
                     @SerializedName("Director")    var directors: String = "",
                     @SerializedName("Writer")      var writers: String = "",
                     @SerializedName("Actors")      var actors: String = "",
                     @SerializedName("Plot")        var plot: String = "")

