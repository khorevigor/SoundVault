package com.dsphoenix.soundvault.data

import com.dsphoenix.soundvault.data.model.Track
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService

class AudioRepository(
    private val firestoreService: FirestoreService,
    private val firebaseStorage: FirebaseStorageService
) {
    fun uploadFile (track: Track) {
        val updatedTrack = firestoreService.writeTrack(track)
        firebaseStorage.uploadTrack(updatedTrack)
    }
}