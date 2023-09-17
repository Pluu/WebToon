package com.pluu.webtoon.sample.di

import com.pluu.webtoon.navigator.MainContainerNavigator
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

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal abstract class NavigatorModuleBinds {
    @Binds
    abstract fun bindsMainContainerNavigator(navigator: BaseSampleNavigator): MainContainerNavigator
}