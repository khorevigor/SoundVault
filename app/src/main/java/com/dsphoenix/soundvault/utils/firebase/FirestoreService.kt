package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dsphoenix.soundvault.data.model.Track
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "FirestoreService"

private const val AUDIO_TRACKS_COLLECTION = "audioTracks"

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
        db.collection(AUDIO_TRACKS_COLLECTION).add(item)
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot added with id ${it.id}")
            }.addOnFailureListener { e ->
                Log.w(TAG, "Document creation error", e)
            }
        return updatedTrack
    }

    fun getTracks(): LiveData<List<Track>> {
        return getCollection(AUDIO_TRACKS_COLLECTION)
    }

    private fun getCollection(collection: String): LiveData<List<Track>> {
        val tracks = MutableLiveData<List<Track>>()
        db.collection(collection).get()
            .addOnSuccessListener { result ->
                tracks.value = result.documents.map { documentSnapshot ->
                    Log.d(TAG, "${documentSnapshot.id} => ${documentSnapshot.data}}")
                    documentSnapshot.toObject<Track>() ?: Track()
                }
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Collection pulling error", e)
            }
        return tracks
    }
}