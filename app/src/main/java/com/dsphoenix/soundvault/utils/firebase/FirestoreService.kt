package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

private const val TAG = "FirestoreService"

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun writeTrack(track: Track): Track {
        val remotePath = "audio/${track.name}"
        val item = hashMapOf(
            DbConstants.TRACK_NAME_FIELD to track.name,
            DbConstants.TRACK_DESCRIPTION_FIELD to track.description,
            DbConstants.TRACK_PATH to remotePath
        )

        val updatedTrack: Track = track.copy(path = remotePath)
        try {
            db.collection(DbConstants.TRACK_COLLECTION).add(item).await()
        } catch (cause: FirebaseFirestoreException) {
            Log.d(TAG, "Error uploading file: $cause")
        }
        return updatedTrack
    }

    fun getTracks(): Flow<List<Track>> = flow { emit(getCollection(DbConstants.TRACK_COLLECTION)) }

    private suspend fun getCollection(collection: String): List<Track> =
        db.collection(collection).get().await().map { documentSnapshot ->
            Log.d(TAG, "${documentSnapshot.id} => ${documentSnapshot.data}}")
            documentSnapshot.toObject()
        }
}