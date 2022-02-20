package com.tvtracker.ui.main

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tvtracker.dto.MediaItem
import com.tvtracker.service.MediaService
import kotlinx.coroutines.launch

class BrowseViewModel(var mediaService: MediaService = MediaService()): ViewModel() {

    val PAGE_SIZE = 10

    var searchTxt = "movie"
    var searchType = "movie"
    var page by mutableStateOf(1)
    var loading by mutableStateOf(false)

    private var scrollPosition = 0

    private var currentSearchTxt = ""
    private var currentSearchType = ""

    var mediaItems: MutableLiveData<List<MediaItem>> = MutableLiveData<List<MediaItem>>()

    init {
        search(null)
    }

    fun search(listState: LazyListState?){
        viewModelScope.launch {
            currentSearchTxt = searchTxt
            currentSearchType = searchType
            page = 1

            val imdbResponse = mediaService.searchIMDB(searchTxt, searchType, page)
            imdbResponse?.let {
                mediaItems.postValue(it.results)
            }

            listState?.scrollToItem(0)
        }
    }

    fun nextPage(){
        viewModelScope.launch {
            // prevent duplicate event due to recompose happening to quickly
            if((scrollPosition + 1) >= (page * PAGE_SIZE) ){
                loading = true
                page += 1

                if(page > 1){
                    val imdbResponse = mediaService.searchIMDB(currentSearchTxt, currentSearchType, page)
                    imdbResponse?.let {
                        appendMediaItems(it.results)
                    }
                }
                loading = false
            }
        }
    }

    fun onChangeScrollPosition(position: Int){
        scrollPosition = position
    }

    private fun appendMediaItems(newMediaItems: List<MediaItem>) {
        val current = ArrayList(mediaItems.value)
        current.addAll(newMediaItems)
        mediaItems.postValue(current)
    }
}