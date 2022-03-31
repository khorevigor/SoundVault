package com.dsphoenix.soundvault.data

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

    fun getTracks(queryParams: Map<String, String>? = null): Flow<List<Track>> = flow {
        if (tracks.isNullOrEmpty()) {
            val query = queryParams?.toMutableMap() ?: mutableMapOf()
            if (!isSubscriptionEnabled) {
                query[DbConstants.TRACK_DISTRIBUTION_PLAN_FIELD] =
                    DistributionPlan.FREE_FOR_ALL.toString()
            }
            val tracksWithNoPaths = firestoreService.getTracksQuery(query)
            tracks = tracksWithNoPaths.map { track ->
                val audioUrl = track.path?.let { path -> firebaseStorage.getTrackAudioUri(path) }
                val imageUrl = track.imagePath?.let { path -> firebaseStorage.getTrackImageUri(path) }
                track.copy(path = audioUrl, imagePath = imageUrl)
            }
        }
        emit(tracks)
    }

    fun getTrack(id: String): Flow<Track> = flow {
        var track = tracks.find { it.id == id }
        if (track == null) {
            val incompleteTrack = firestoreService.getTrackById(id) as Track
            val audioUrl = incompleteTrack.path?.let { path -> firebaseStorage.getTrackAudioUri(path) }
            val imageUrl = incompleteTrack.imagePath?.let { path -> firebaseStorage.getTrackImageUri(path) }
            track = incompleteTrack.copy(path = audioUrl, imagePath = imageUrl)
        }
        emit(track)
    }

    companion object {
        var isSubscriptionEnabled = false
    }
}