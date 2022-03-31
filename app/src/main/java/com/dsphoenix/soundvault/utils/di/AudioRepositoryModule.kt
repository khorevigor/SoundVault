package com.dsphoenix.soundvault.utils.di

import com.dsphoenix.soundvault.data.AudioRepository
import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AudioRepositoryModule {
    @Singleton
    @Provides
    fun provideAudioRepository(
        firestoreService: FirestoreService,
        firebaseStorageService: FirebaseStorageService
    ): AudioRepository = AudioRepository(firestoreService, firebaseStorageService)
}