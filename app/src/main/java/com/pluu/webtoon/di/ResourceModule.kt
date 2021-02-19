package com.pluu.webtoon.di

import android.content.Context
import com.pluu.webtoon.di.provider.ColorProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object ResourceModule {
    @Singleton
    @Provides
    fun provideColorProvider(
        @ApplicationContext context: Context
    ): ColorProvider = ColorProvider(context)
}