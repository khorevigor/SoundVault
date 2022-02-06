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
    suspend fun uploadTrack(track: Track) {
        val updatedTrack = firestoreService.writeTrack(track)
        firebaseStorage.uploadTrack(updatedTrack)
    }

    fun getTracks(queryParams: Map<String, String>? = null): Flow<List<Track>> = flow {
        val query = queryParams?.toMutableMap() ?: mutableMapOf()
        if (!isSubscriptionEnabled) {
            query[DbConstants.TRACK_DISTRIBUTION_PLAN_FIELD] = DistributionPlan.FREE_FOR_ALL.toString()
        }
        emit(firestoreService.getTracksQuery(query))
    }

    companion object {
        var isSubscriptionEnabled = false
    }
}