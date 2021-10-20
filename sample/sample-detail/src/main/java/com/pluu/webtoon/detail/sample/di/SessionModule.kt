package com.pluu.webtoon.detail.sample.di

import com.pluu.webtoon.model.NAV_ITEM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object SessionModule {
    @Provides
    fun provideNavType(): NAV_ITEM = NAV_ITEM.NAVER
}
