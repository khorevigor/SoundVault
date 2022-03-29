package com.dsphoenix.soundvault.data

import android.util.Log
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.dsphoenix.soundvault.utils.constants.DistributionPlan
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AudioRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val firebaseStorage: FirebaseStorageService
) {
    private var tracks = listOf<Track>()

    suspend fun uploadTrack(track: Track) {
        val updatedTrack = firestoreService.writeTrack(track)
        firebaseStorage.uploadTrack(updatedTrack)
    }

    fun getTrackImageRef(track: Track) = flow {
        emit(track.imagePath?.let { firebaseStorage.getTrackImageUri(it) })
    }

    fun getTrackAudioRef(track: Track) = flow {
        emit(track.path?.let { firebaseStorage.getTrackAudioUri(it) })
    }

    fun getTracks(queryParams: Map<String, String>? = null): Flow<List<Track>> = flow {
        if (tracks.isNullOrEmpty()) {
            val query = queryParams?.toMutableMap() ?: mutableMapOf()
            if (!isSubscriptionEnabled) {
                query[DbConstants.TRACK_DISTRIBUTION_PLAN_FIELD] =
                    DistributionPlan.FREE_FOR_ALL.toString()
            }
            tracks = firestoreService.getTracksQuery(query)
        }
        emit(tracks)
    }

    fun getTrack(id: String): Flow<Track> = flow {
        var track = tracks.find { it.id == id }
        if (track == null) {
            track = firestoreService.getTrackById(id) as Track
        }
        emit(track)
    }

    companion object {
        var isSubscriptionEnabled = false
    }
}