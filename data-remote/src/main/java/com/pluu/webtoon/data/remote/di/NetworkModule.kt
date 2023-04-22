package com.pluu.webtoon.data.remote.di

import com.pluu.webtoon.data.remote.network.INetworkUseCase
import com.pluu.webtoon.data.remote.network.NetworkUseCase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object NetworkModule {
    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

@Suppress("unused")
@InstallIn(SingletonComponent::class)
@Module
internal abstract class NetworkModuleBinds {
    @Binds
    abstract fun bindsNetworkUseCase(
        networkUseCase: NetworkUseCase
    ): INetworkUseCase
}