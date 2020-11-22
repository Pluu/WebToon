package com.pluu.webtoon.detail.di

import com.pluu.webtoon.detail.navigator.DetailNavigatorImpl
import com.pluu.webtoon.navigator.DetailNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class DetailModule {
    @Binds
    abstract fun provideDetailNavigator(
        navigator: DetailNavigatorImpl
    ): DetailNavigator
}