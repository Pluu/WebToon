package com.pluu.webtoon.data.di

import com.pluu.webtoon.data.repository.SessionRepository
import com.pluu.webtoon.model.NAV_ITEM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object SessionModule {
    @Provides
    fun provideNavType(
        repository: SessionRepository
    ): NAV_ITEM = repository.getDefaultWebToon()
}
