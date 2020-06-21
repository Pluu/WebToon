package com.pluu.webtoon.di

import com.pluu.webtoon.AppNavigator
import com.pluu.webtoon.ui.WebToonNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class NavigatorModule {
    @Binds
    abstract fun provideAppNavigator(webToonNavigator: WebToonNavigator): AppNavigator
}
