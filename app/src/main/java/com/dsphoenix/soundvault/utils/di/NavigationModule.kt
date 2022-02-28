package com.dsphoenix.soundvault.utils.di

import com.dsphoenix.soundvault.utils.navigation.NavigationController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class NavigationModule {
    @Provides
    fun provideNavigationController(
    ): NavigationController = NavigationController()
}