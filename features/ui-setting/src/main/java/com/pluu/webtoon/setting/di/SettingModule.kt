package com.pluu.webtoon.setting.di

import com.pluu.webtoon.navigator.SettingNavigator
import com.pluu.webtoon.setting.navigator.SettingNavigatorImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
internal abstract class SettingModule {
    @Binds
    abstract fun provideSettingNavigator(
        navigator: SettingNavigatorImpl
    ): SettingNavigator
}
