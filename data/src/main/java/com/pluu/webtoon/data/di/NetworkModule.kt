package com.pluu.webtoon.data.di

import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.NetworkUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun provideNetworkUseCase(
        okHttpClient: OkHttpClient
    ): INetworkUseCase = NetworkUseCase(okHttpClient)

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}