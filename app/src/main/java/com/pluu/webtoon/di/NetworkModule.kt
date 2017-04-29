package com.pluu.webtoon.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

/**
 * OkHttp Network Provider
 * Created by pluu on 2017-04-30.
 */
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHeater(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

}
