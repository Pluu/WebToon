package com.pluu.webtoon.episode.sample.di

import com.pluu.webtoon.model.NAV_ITEM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object SessionModule {
    @Provides
    fun provideNavType(): NAV_ITEM = NAV_ITEM.NAVER
}
