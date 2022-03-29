package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.TAG
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.ktx.storage
import io.grpc.NameResolver
import kotlinx.coroutines.tasks.await
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class FirebaseStorageService {
    private val storage = Firebase.storage

    suspend fun uploadTrack(track: Track) {
        try {
            track.apply {
                assertNotNull(path, uri, imagePath, imageUri)
            }

            val storageRef = storage.reference
            storageRef.child(track.path!!).putFile(track.uri!!).await()
            storageRef.child(track.imagePath!!).putFile(track.imageUri!!).await()
        } catch (cause: IllegalStateException) {
            Log.d(TAG, "all path and URI fields should not be null")
            Log.d(TAG, cause.toString())
        } catch (cause: StorageException) {
            Log.d(TAG, "error uploading file:")
            Log.d(TAG, cause.toString())
        }
    }

    suspend fun getTrackImageUri(path: String) =
        storage.reference.child(path).downloadUrl.await().toString()

    suspend fun getTrackAudioUri(path: String) =
        storage.reference.child(path).downloadUrl.await().toString()

    private fun assertNotNull(vararg args: Any?) {
        args.map { checkNotNull(it) }
    }
}