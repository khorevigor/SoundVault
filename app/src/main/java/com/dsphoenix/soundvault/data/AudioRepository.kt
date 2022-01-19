package com.dsphoenix.soundvault.data

import androidx.lifecycle.LiveData
import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import javax.inject.Inject

class AudioRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val firebaseStorage: FirebaseStorageService
) {
    fun uploadTrack(track: Track) {
        val updatedTrack = firestoreService.writeTrack(track)
        firebaseStorage.uploadTrack(updatedTrack)
    }

    fun getTracks(): LiveData<List<Track>> {
        return firestoreService.getTracks()
    }
}