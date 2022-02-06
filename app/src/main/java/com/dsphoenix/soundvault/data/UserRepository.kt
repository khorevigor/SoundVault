package com.dsphoenix.soundvault.data

import com.dsphoenix.soundvault.data.model.User
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

const val TAG = "UserRepository"

class UserRepository @Inject constructor(
    private val firestoreService: FirestoreService
)
{
    suspend fun getUser(uid: String): Flow<User> = flow {
        emit(fetchUser(uid))
    }

    suspend fun updateUser(user: User) {
        firestoreService.writeUser(user)
    }

    private suspend fun fetchUser(uid: String): User = firestoreService.getOrCreateUser(uid)
}