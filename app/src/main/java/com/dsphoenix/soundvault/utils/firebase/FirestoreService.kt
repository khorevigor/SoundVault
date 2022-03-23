package com.dsphoenix.soundvault.utils.firebase

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.TAG
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.lang.IllegalStateException

class FirestoreService {
    private val db = FirebaseFirestore.getInstance()

    suspend fun writeTrack(track: Track): Track {
        val trackAuthorAndName = "${track.authorName}_${track.name}"
        val remotePath = "audio/$trackAuthorAndName"
        val remoteCoverPath = "image/$trackAuthorAndName"
        var updatedTrack: Track = track.copy(path = remotePath, imagePath = remoteCoverPath)

        DbConstants.apply {
            val item = hashMapOf(
                TRACK_NAME_FIELD to updatedTrack.name,
                TRACK_AUTHOR_NAME to updatedTrack.authorName,
                TRACK_DESCRIPTION_FIELD to updatedTrack.description,
                TRACK_PATH_FIELD to updatedTrack.path,
                TRACK_IMAGE_PATH_FIELD to updatedTrack.imagePath,
                TRACK_GENRES_FIELD to updatedTrack.genres,
                TRACK_DISTRIBUTION_PLAN_FIELD to updatedTrack.distributionPlan,
                TRACK_DISTRIBUTION_BUNDLE_FIELD to updatedTrack.distributionBundle,
                TRACK_SINGLE_PRICE_FIELD to updatedTrack.singlePrice
            )
            try {
                val id = db.collection(TRACK_COLLECTION).add(item).await().id
                updatedTrack = updatedTrack.copy(id = id)
                db.collection(TRACK_COLLECTION).document(id).update(TRACK_ID_FIELD, id)
            } catch (cause: FirebaseFirestoreException) {
                Log.d(TAG, "Error uploading file: $cause")
            }
        }
        Log.d(TAG, "returning track $track")
        return updatedTrack
    }

    suspend fun getTrackById(id: String): Track? =
        db.collection(DbConstants.TRACK_COLLECTION).document(id).get().await().toObject()

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