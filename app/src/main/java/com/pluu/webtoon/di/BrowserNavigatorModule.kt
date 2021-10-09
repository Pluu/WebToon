package com.pluu.webtoon.di

import com.pluu.webtoon.navigator.BrowserNavigator
import com.pluu.webtoon.navigator.BrowserNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
abstract class BrowserNavigatorModule {
    @Binds
    abstract fun provideBrowserNavigator(
        navigator: BrowserNavigatorImpl
    ): BrowserNavigator
}