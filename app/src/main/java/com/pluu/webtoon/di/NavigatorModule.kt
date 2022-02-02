package com.pluu.webtoon.di

import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.BrowserNavigatorImpl
import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.navigator.WeeklyNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModule {
    @Binds
    abstract fun provideBrowserNavigator(
        navigator: BrowserNavigatorImpl
    ): BrowserNavigator

    @Binds
    abstract fun provideWeeklyNavigator(
        navigator: WeeklyNavigatorImpl
    ): WeeklyNavigator
}