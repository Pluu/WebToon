package com.pluu.webtoon.di

import com.pluu.webtoon.db.RealmHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module

val dbModule = module {
    single { RealmHelper() }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
    single { NetworkUseCase(get()) }
}
