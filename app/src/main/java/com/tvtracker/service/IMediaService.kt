package com.tvtracker.service

import com.tvtracker.dto.ImdbResponse
import com.tvtracker.dto.MediaItem
import com.tvtracker.dto.MoviesWatched

interface IMediaService {
    suspend fun searchImdb(text: String, type: String, page: Int): ImdbResponse?
    suspend fun searchByImdbId(imdbId: String): MediaItem?
    suspend fun addWatchedMovie(userId: Int, movieId: Int): MoviesWatched
}
