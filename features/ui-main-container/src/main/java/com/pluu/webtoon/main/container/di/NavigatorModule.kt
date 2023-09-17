package com.pluu.webtoon.main.container.di

import com.pluu.webtoon.main.container.navigation.MainContainerNavigatorImpl
import com.pluu.webtoon.navigator.MainContainerNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModule {
    @Binds
    abstract fun provideMainContainerNavigator(
        navigator: MainContainerNavigatorImpl
    ): MainContainerNavigator
}