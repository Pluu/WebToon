package com.pluu.webtoon.weekly.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModule {
//    @Binds
//    abstract fun provideWeeklyNavigator(
//        navigator: WeeklyNavigatorImpl
//    ): WeeklyNavigator
}