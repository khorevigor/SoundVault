package com.dsphoenix.soundvault.data

import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.constants.DbConstants
import com.dsphoenix.soundvault.utils.constants.DistributionPlan
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AudioRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val firebaseStorage: FirebaseStorageService
) {
    private var tracksCache = emptyList<Track>()

    fun getTracks(queryParams: Map<String, String>? = null, skipCache: Boolean = false): Flow<List<Track>> = flow {
        if (!skipCache) {
            emit (tracksCache)
        }
        tracksCache = fetchTracks(queryParams)
        emit (tracksCache)
    }

    fun getTrack(id: String): Flow<Track> = flow {
        var track = tracksCache.find { it.id == id }
        if (track == null) {
            val incompleteTrack = firestoreService.getTrackById(id) as Track
            val audioUrl = incompleteTrack.path?.let { path -> firebaseStorage.getTrackAudioUri(path) }
            val imageUrl = incompleteTrack.imagePath?.let { path -> firebaseStorage.getTrackImageUri(path) }
            track = incompleteTrack.copy(path = audioUrl, imagePath = imageUrl)
        }
        emit(track)
    }

    suspend fun uploadTrack(track: Track) {
        val updatedTrack = firestoreService.writeTrack(track)
        firebaseStorage.uploadTrack(updatedTrack)
    }

    private suspend fun fetchTracks(queryParams: Map<String, String>? = null) : List<Track> {
        val query = queryParams?.toMutableMap() ?: mutableMapOf()
        if (!isSubscriptionEnabled) {
            query[DbConstants.TRACK_DISTRIBUTION_PLAN_FIELD] =
                DistributionPlan.FREE_FOR_ALL.toString()
        }
        val tracksWithNoPaths = firestoreService.getTracksQuery(query)
        return tracksWithNoPaths.map { track ->
            val audioUrl = track.path?.let { path -> firebaseStorage.getTrackAudioUri(path) }
            val imageUrl = track.imagePath?.let { path -> firebaseStorage.getTrackImageUri(path) }
            track.copy(path = audioUrl, imagePath = imageUrl)
        }
    }

    companion object {
        var isSubscriptionEnabled = false
    }
}