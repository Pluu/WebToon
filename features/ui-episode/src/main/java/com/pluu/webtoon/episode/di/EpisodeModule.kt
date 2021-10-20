package com.pluu.webtoon.episode.di

import com.pluu.webtoon.episode.navigator.EpisodeNavigatorImpl
import com.pluu.webtoon.navigator.EpisodeNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class EpisodeModule {
    @Binds
    abstract fun provideEpisodeNavigator(
        navigator: EpisodeNavigatorImpl
    ): EpisodeNavigator
}