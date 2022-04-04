package com.tvtracker.service

import com.tvtracker.api.ImdbRetrofitClientInstance
import com.tvtracker.dao.IMediaItemDAO
import com.tvtracker.dto.ImdbResponse
import com.tvtracker.dto.MediaItem
import com.tvtracker.dto.MoviesWatched
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class MediaService : IMediaService {

    override suspend fun searchImdb(text: String, type: String, page: Int): ImdbResponse? {
        return withContext(Dispatchers.IO) {
            val service = ImdbRetrofitClientInstance.retrofitInstance?.create(IMediaItemDAO::class.java)
            val mediaItems = async {service?.searchImdb(text, type, page)}
            return@withContext mediaItems.await()?.awaitResponse()?.body()
        }
    }

    override suspend fun searchByImdbId(imdbId: String): MediaItem? {
        return withContext(Dispatchers.IO) {
            val service = ImdbRetrofitClientInstance.retrofitInstance?.create(IMediaItemDAO::class.java)
            val mediaItems = async {service?.searchByImdbId(imdbId)}
            return@withContext mediaItems.await()?.awaitResponse()?.body()
        }
    }

    override suspend fun addWatchedMovie(userId: Int, movieId: Int): MoviesWatched {
        return withContext(Dispatchers.IO) {
            val service = ImdbRetrofitClientInstance.retrofitInstance?.create(IMediaItemDAO::class.java)
            val mediaItems = async {service?.addWatchedMovie(movieId)}
            return@withContext mediaItems.await()?.awaitResponse()?.body()
        }
    }
}