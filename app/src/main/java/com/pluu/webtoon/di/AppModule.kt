package com.pluu.webtoon.di

import com.pluu.support.impl.ColorProvider
import com.pluu.support.impl.NaviColorProvider
import com.pluu.webtoon.db.RealmHelper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module.module

private val dbModule = module {
    single { RealmHelper() }
}

private val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
    single<INetworkUseCase>(AppProperties.NETWORK) {
        NetworkUseCase(get())
    }
}

private val resourceModule = module {
    single { ColorProvider(get()) }
    factory { NaviColorProvider(get(), getProperty(ServiceProperties.NAV_ITEM)) }
}

val appModule = arrayOf(dbModule, networkModule, resourceModule)

object AppProperties {
    const val NETWORK = "NETWORK"
}
