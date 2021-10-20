package com.pluu.webtoon.weekly.di

import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.weekly.model.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal object SessionModule {
    @Provides
    fun provideNavType(
        session: Session
    ): NAV_ITEM = session.navi
}
