package com.pluu.webtoon.setting.di

import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.setting.ui.SettingNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@InstallIn(ApplicationComponent::class)
@Module
abstract class SettingModule {
    @Binds
    abstract fun provideSettingNavigator(navigator: SettingNavigatorImpl): SettingNavigator
}
