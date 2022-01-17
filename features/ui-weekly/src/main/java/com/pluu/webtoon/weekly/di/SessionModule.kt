package com.pluu.webtoon.weekly.di

import com.pluu.webtoon.domain.usecase.GetDefaultNaviUseCase
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
        defaultNaviUseCase: GetDefaultNaviUseCase
    ): NAV_ITEM = defaultNaviUseCase()
}
