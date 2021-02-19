package com.pluu.webtoon.setting.di

import android.content.SharedPreferences
import com.pluu.compose.ambient.PreferenceProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
object PreferenceModule {
    @Provides
    fun providePreferenceProvider(
        preferences: SharedPreferences
    ): PreferenceProvider = PreferenceProvider(preferences)
}
