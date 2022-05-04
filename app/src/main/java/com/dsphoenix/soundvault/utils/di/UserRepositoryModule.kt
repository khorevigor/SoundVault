package com.dsphoenix.soundvault.utils.di

import com.dsphoenix.soundvault.data.UserRepository
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UserRepositoryModule {

    @Provides
    fun provideUserRepository(
        firestoreService: FirestoreService,
        firebaseStorageService: FirebaseStorageService
    ): UserRepository = UserRepository(firestoreService, firebaseStorageService)
}