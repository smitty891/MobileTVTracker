package com.tvtracker.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.tvtracker.dto.MediaItem
import com.tvtracker.dto.User

class FirebaseRepository {

    fun saveMediaItem(selectedMediaItem: MediaItem, user: User, firestore: FirebaseFirestore) {
        user.let { user ->
            val document =
                if (selectedMediaItem.id.isEmpty()) {
                    firestore.collection("users").document(user.uid).collection("mediaItems")
                        .document()
                } else {
                    firestore.collection("users").document(user.uid).collection("mediaItems")
                        .document(selectedMediaItem.id)
                }
            selectedMediaItem.id = document.id
            val handle = document.set(selectedMediaItem)
            handle.addOnSuccessListener { Log.d("Firebase", "Document Saved") }
            handle.addOnFailureListener { Log.e("Firebase", "Error: $it") }
        }
    }

    fun deleteMediaItem(selectedMediaItem: MediaItem, user: User, firestore: FirebaseFirestore) {
        user.let { user ->
            val document =
                firestore.collection("users").document(user.uid).collection("mediaItems")
                    .document(selectedMediaItem.id)
            val handle = document.delete()
            handle.addOnSuccessListener { Log.d("Firebase", "Document Deleted") }
            handle.addOnFailureListener { Log.e("Firebase", "Error: $it") }
        }

    }

    fun saveUser(user: User, firestore: FirebaseFirestore) {
        user.let {
            firestore.collection("users").document(it.uid).set(it)
        }
    }
}