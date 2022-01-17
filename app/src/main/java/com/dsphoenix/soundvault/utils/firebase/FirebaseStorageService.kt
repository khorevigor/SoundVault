package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

private const val TAG = "FirebaseStorageService"

object FirebaseStorageService {
    private val storage = Firebase.storage

    fun uploadTrack(track: Track) {
        track.remotePath?.let {
            val storageRef = storage.reference.child(it)
            track.uri?.let { it1 ->
                storageRef.putFile(it1).addOnSuccessListener {
                    Log.d(TAG, "File uploaded")
                }.addOnFailureListener { e ->
                    Log.d(TAG, "File uploading error", e)
                }
            }
        }
    }
}