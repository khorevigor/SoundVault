package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

private const val TAG = "FirestoreService"

private const val AUDIO_TRACKS_COLLECTION = "audioTracks"

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun writeTrack(track: Track): Track {
        val remotePath = "audio/${track.name}"
        val item = hashMapOf(
            "name" to track.name,
            "description" to track.description,
            "path" to remotePath
        )

        val updatedTrack: Track = track.copy(remotePath = remotePath)
        try {
            db.collection(AUDIO_TRACKS_COLLECTION).add(item).await()
        } catch (cause: FirebaseFirestoreException) {
            Log.d(TAG, "Error uploading file: $cause")
        }
        return updatedTrack
    }

    fun getTracks(): Flow<List<Track>> = flow { emit(getCollection(AUDIO_TRACKS_COLLECTION)) }

    private suspend fun getCollection(collection: String): List<Track> =
        db.collection(collection).get().await().map { documentSnapshot ->
            Log.d(TAG, "${documentSnapshot.id} => ${documentSnapshot.data}}")
            documentSnapshot.toObject()
        }
}