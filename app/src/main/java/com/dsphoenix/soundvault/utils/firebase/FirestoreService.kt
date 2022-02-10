package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

private const val TAG = "FirestoreService"

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun writeTrack(track: Track): Track {
        val remotePath = "audio/${track.name}"
        val updatedTrack: Track = track.copy(path = remotePath)

        val item = hashMapOf(
            DbConstants.TRACK_NAME_FIELD to track.name,
            DbConstants.TRACK_DESCRIPTION_FIELD to track.description,
            DbConstants.TRACK_PATH to track.path,
            DbConstants.TRACK_DISTRIBUTION_PLAN_FIELD to track.distributionPlan
        )

        try {
            db.collection(DbConstants.TRACK_COLLECTION).add(item).await()
        } catch (cause: FirebaseFirestoreException) {
            Log.d(TAG, "Error uploading file: $cause")
        }
        return updatedTrack
    }

    suspend fun getTracksQuery(queryParams: Map<String, String>): List<Track> =
        getCollectionQuery(DbConstants.TRACK_COLLECTION, queryParams)

    suspend fun getOrCreateUser(uid: String): User {
        var user: User? =
            db.collection(DbConstants.USERS_COLLECTION).document(uid).get().await().toObject()

        if (user == null) {
            Log.d(TAG, "No user with uid = $uid found. Creating new document")
            user = User(uid, false)
            writeUser(user)
        }

        return user
    }

    suspend fun writeUser(user: User) {
        try {
            checkNotNull(user.uid)
            db.collection(DbConstants.USERS_COLLECTION).document(user.uid).set(user).await()
        } catch (cause: IllegalStateException) {
            Log.d(TAG, "uid should not be null")
            Log.d(TAG, cause.toString())
        } catch (cause: FirebaseFirestoreException) {
            Log.d(TAG, "error creating user file:")
            Log.d(TAG, cause.toString())
        }
    }

    private suspend inline fun <reified T> getCollectionQuery(
        collectionName: String,
        queryParams: Map<String, String>? = null
    ): List<T> {
        var query: Query = db.collection(collectionName)

        queryParams?.map {
            query = query.whereEqualTo(it.key, it.value)
        }

        return query.get().await().map { documentSnapshot ->
            Log.d(TAG, "${documentSnapshot.id} => ${documentSnapshot.data}}")
            documentSnapshot.toObject()
        }
    }
}