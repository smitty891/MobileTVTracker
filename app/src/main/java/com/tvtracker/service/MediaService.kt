package com.tvtracker.service

import com.tvtracker.api.ImdbRetrofitClientInstance
import com.tvtracker.dao.IMediaItemDAO
import com.tvtracker.dto.ImdbResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse


class MediaService : IMediaService {

    override suspend fun searchIMDB(text: String, type: String, page: Int): ImdbResponse? {
        return withContext(Dispatchers.IO) {
            val service = ImdbRetrofitClientInstance.retrofitInstance?.create(IMediaItemDAO::class.java)
            val mediaItems = async {service?.searchIMDB(text, type, page)}
            return@withContext mediaItems.await()?.awaitResponse()?.body()
        }
    }
}