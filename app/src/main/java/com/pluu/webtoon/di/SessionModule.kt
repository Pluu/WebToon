package com.pluu.webtoon.di

import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.common.Session
import com.pluu.webtoon.data.pref.PrefConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
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
