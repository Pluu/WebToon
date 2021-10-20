package com.pluu.webtoon.weekly.di

import com.pluu.webtoon.navigator.WeeklyNavigator
import com.pluu.webtoon.weekly.navigator.WeeklyNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModule {
    @Binds
    abstract fun provideWeeklyNavigator(
        navigator: WeeklyNavigatorImpl
    ): WeeklyNavigator
}