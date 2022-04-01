package com.tvtracker.ui.main

import android.util.Log
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.tvtracker.dto.MediaItem
import com.tvtracker.dto.User
import com.tvtracker.service.IMediaService
import com.tvtracker.service.MediaService
import kotlinx.coroutines.launch

class BrowseViewModel(var mediaService: IMediaService = MediaService()): ViewModel() {

    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    var user: User? = null
    var selectedMediaItem by mutableStateOf(MediaItem())

    val pageSize = 10

    var searchTxt = "movie"
    var searchType = "movie"
    var page by mutableStateOf(1)
    var loading by mutableStateOf(false)

    private var scrollPosition = 0

    private var currentSearchTxt = ""
    private var currentSearchType = ""

    var imdbMediaItems: MutableLiveData<List<MediaItem>> = MutableLiveData<List<MediaItem>>()
    var userMediaItems: MutableLiveData<List<MediaItem>> = MutableLiveData<List<MediaItem>>()

    init {
        firestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        searchImdb(null)
    }

    fun searchImdb(listState: LazyListState?) {
        viewModelScope.launch {
            // Resets search results whenever user searches for something new
            resetMediaItems()

            currentSearchTxt = searchTxt
            currentSearchType = searchType
            page = 1

            loading = true

            val imdbResponse = mediaService.searchImdb(searchTxt, searchType, page)
            imdbResponse?.let {
                imdbMediaItems.postValue(it.results)
            }

            loading = false

            listState?.scrollToItem(0)
        }
    }

    fun nextPage() {
        viewModelScope.launch {
            // prevent duplicate event due to recompose happening to quickly
            if((scrollPosition + 1) >= (page * pageSize) ){
                loading = true
                page += 1

                if(page > 1){
                    val imdbResponse = mediaService.searchImdb(currentSearchTxt, currentSearchType, page)
                    imdbResponse?.let {
                        appendMediaItems(it.results)
                    }
                }
                loading = false
            }
        }
    }

    fun onChangeScrollPosition(position: Int) {
        scrollPosition = position
    }

    private fun resetMediaItems() {
        imdbMediaItems.value = listOf()
    }

    private fun appendMediaItems(newMediaItems: List<MediaItem>) {
        val current = ArrayList(imdbMediaItems.value)
        current.addAll(newMediaItems)
        imdbMediaItems.postValue(current)
    }

    fun getMediaItemDetails() {
        viewModelScope.launch {
            if(selectedMediaItem.imdbId.isNotEmpty()) {
                loading = true
                val detailedMediaItem = mediaService.searchByImdbId(selectedMediaItem.imdbId)
                detailedMediaItem?.let {
                    selectedMediaItem = it
                }
                loading = false
            }
        }
    }

    fun listenToUserMediaItems() {
        user?.let {
            firestore.collection("users").document(it.uid).collection("mediaItems").addSnapshotListener {
                snapshot, e ->

                if (e != null) {
                    Log.w("Listen failed", e)
                    return@addSnapshotListener
                }

                snapshot?.let {
                    val mediaItems = ArrayList<MediaItem>()
                    val documents = snapshot.documents
                    documents.forEach {
                        val mediaItem = it.toObject(MediaItem::class.java)
                        mediaItem?.let {
                            mediaItems.add(it)
                        }
                    }
                    userMediaItems.postValue(mediaItems)
                }
            }
        }
    }

    fun saveMediaItem() {
        if(selectedMediaItem.imdbId.isNotEmpty()) {
            user?.let { user ->
                val document =
                    if (selectedMediaItem.id.isEmpty()) {
                        firestore.collection("users").document(user.uid).collection("mediaItems").document()
                    } else {
                        firestore.collection("users").document(user.uid).collection("mediaItems").document(selectedMediaItem.id)
                    }
                selectedMediaItem.id = document.id
                val handle = document.set(selectedMediaItem)
                handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
                handle.addOnFailureListener { Log.e("Firebase", "Error: $it") }
            }
        }
    }

    fun deleteMediaItem() {
        if(selectedMediaItem.id.isNotEmpty()) {
            user?.let { user ->
                val document = firestore.collection("users").document(user.uid).collection("mediaItems").document(selectedMediaItem.id)
                val handle = document.delete()
                handle.addOnSuccessListener { Log.d("Firebase", "Document Deleted") }
                handle.addOnFailureListener { Log.e("Firebase", "Error: $it") }
            }
        }
    }

    fun saveUser() {
        user?.let {
            firestore.collection("users").document(it.uid).set(it)
        }
    }
}