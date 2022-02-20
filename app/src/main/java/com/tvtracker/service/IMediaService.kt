package com.tvtracker.service

import com.tvtracker.dto.ImdbResponse

interface IMediaService {
    suspend fun searchIMDB(text: String, type: String, page: Int): ImdbResponse?
}
