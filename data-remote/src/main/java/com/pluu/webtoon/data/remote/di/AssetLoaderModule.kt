package com.pluu.webtoon.data.remote.di

import android.content.Context
import com.pluu.webtoon.data.remote.utils.ResourceLoader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object AssetLoaderModule {
    @Provides
    @Singleton
    fun bindAssetLoader(
        @ApplicationContext context: Context
    ): ResourceLoader = ResourceLoader(context.resources)
}