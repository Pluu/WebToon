package com.pluu.webtoon.weekly.di

import com.pluu.webtoon.data.pref.PrefConfig
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.weekly.model.Session
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object SessionModule {
    @Singleton
    @Provides
    fun provideSession(
        prefConfig: PrefConfig
    ): Session = Session(prefConfig)

    @Provides
    fun provideNavType(
        session: Session
    ): NAV_ITEM = session.navi
}
