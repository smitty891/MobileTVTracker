package com.tvtracker.service

import com.tvtracker.dto.ImdbResponse
import com.tvtracker.dto.MediaItem

interface IMediaService {
    suspend fun searchImdb(text: String, type: String, page: Int): ImdbResponse?
    suspend fun searchByImdbId(imdbId: String): MediaItem?
}
