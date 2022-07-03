package com.pluu.webtoon.data.di

import com.pluu.webtoon.model.CurrentSession
import com.pluu.webtoon.model.NAV_ITEM
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal object SessionModule {
    @Provides
    fun provideNavType(
        session: CurrentSession
    ): NAV_ITEM = session.navi
}
