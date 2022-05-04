package com.dsphoenix.soundvault.data

import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import javax.inject.Inject

const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val firestoreService: FirestoreService,
    private val firebaseStorageService: FirebaseStorageService
) {
    suspend fun getUser(uid: String): User = fetchUser(uid)

    suspend fun updateUser(user: User) {
        val user = firestoreService.writeUser(user)
        firebaseStorageService.uploadUser(user)
    }

    private suspend fun fetchUser(uid: String): User {
        val incompleteUser = firestoreService.getOrCreateUser(uid)
        val avatarUrl = incompleteUser.avatarPath?.let { firebaseStorageService.getUserAvatarUrl(it) }

        return incompleteUser.copy(avatarPath = avatarUrl)
    }
}