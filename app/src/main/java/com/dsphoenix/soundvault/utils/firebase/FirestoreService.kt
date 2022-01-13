package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.google.firebase.firestore.FirebaseFirestore

private const val TAG = "FirestoreService"

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    fun writeTrack(track: Track): Track {
        val remotePath = "audio/${track.name}"
        val item = hashMapOf(
            "name" to track.name,
            "description" to track.description,
            "path" to remotePath
        )

        val updatedTrack: Track = track.copy(remotePath = remotePath)
        db.collection("audioTracks").add(item)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with id ${it.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Document creation error", e)
            }
        return updatedTrack
    }
}