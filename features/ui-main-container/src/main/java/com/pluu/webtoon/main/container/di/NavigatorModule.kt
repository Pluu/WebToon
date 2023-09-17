package com.pluu.webtoon.main.container.di

import com.pluu.webtoon.main.container.navigation.WeeklyNavigatorImpl
import com.pluu.webtoon.navigator.WeeklyNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModule {
    @Binds
    abstract fun provideWeeklyNavigator(
        navigator: WeeklyNavigatorImpl
    ): WeeklyNavigator
}