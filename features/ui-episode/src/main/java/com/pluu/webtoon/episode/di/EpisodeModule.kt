package com.pluu.webtoon.episode.di

import com.pluu.webtoon.episode.ui.EpisodeNavigatorImpl
import com.pluu.webtoon.navigator.EpisodeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class EpisodeModule {
    @Binds
    abstract fun provideEpisodeNavigator(navigator: EpisodeNavigatorImpl): EpisodeNavigator
}