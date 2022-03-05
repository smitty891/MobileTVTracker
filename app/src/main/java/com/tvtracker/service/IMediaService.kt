package com.tvtracker.service

import com.tvtracker.dto.ImdbResponse

/**
 * provides a suspended search until search is continued
 */
interface IMediaService {
    suspend fun searchIMDB(text: String, type: String, page: Int): ImdbResponse?
}
