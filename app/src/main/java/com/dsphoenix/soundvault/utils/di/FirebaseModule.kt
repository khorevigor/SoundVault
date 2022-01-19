package com.dsphoenix.soundvault.utils.di

import com.dsphoenix.soundvault.utils.firebase.FirebaseStorageService
import com.dsphoenix.soundvault.utils.firebase.FirestoreService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseStorageService(): FirebaseStorageService = FirebaseStorageService()

    @Singleton
    @Provides
    fun provideFirestoreService(): FirestoreService = FirestoreService()
}