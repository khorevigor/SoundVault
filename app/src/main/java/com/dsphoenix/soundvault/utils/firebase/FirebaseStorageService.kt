package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.TAG
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class FirebaseStorageService {
    private val storage = Firebase.storage

    suspend fun uploadTrack(track: Track) {
        try {
            checkNotNull(track.path)
            checkNotNull(track.uri)

            val storageRef = storage.reference.child(track.path)
            storageRef.putFile(track.uri).await()
        }
        catch (cause: IllegalStateException) {
            Log.d(TAG, "remotePath and Uri should not be null")
            Log.d(TAG, cause.toString())
        }
        catch (cause: StorageException) {
            Log.d(TAG, "error uploading file:")
            Log.d(TAG, cause.toString())
        }
    }
}