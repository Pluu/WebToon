package com.pluu.webtoon.sample.di

import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.DetailNavigator
import com.pluu.webtoon.navigator.EpisodeNavigator
import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.sample.BaseSampleNavigator
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object NavigatorModule {
    @Singleton
    @Provides
    fun provideSampleNavigator(): BaseSampleNavigator = BaseSampleNavigator()
}

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModuleBinds {
    @Binds
    abstract fun bindsWeeklyNavigator(navigator: BaseSampleNavigator): WeeklyNavigator

    @Binds
    abstract fun bindsEpisodeNavigator(navigator: BaseSampleNavigator): EpisodeNavigator

    @Binds
    abstract fun bindsDetailNavigator(navigator: BaseSampleNavigator): DetailNavigator

    @Binds
    abstract fun bindsBrowserNavigator(navigator: BaseSampleNavigator): BrowserNavigator

    @Binds
    abstract fun bindsSettingNavigator(navigator: BaseSampleNavigator): SettingNavigator
}