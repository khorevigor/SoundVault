package com.dsphoenix.soundvault.utils.di

import com.dsphoenix.soundvault.utils.mediaplayer.MediaPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MediaPlayerModule {

    @Singleton
    @Provides
    fun provideMediaPlayer(): MediaPlayer = MediaPlayer()
}