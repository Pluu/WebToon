package com.pluu.webtoon.di

import com.pluu.support.impl.ColorProvider
import com.pluu.support.impl.NaviColorProvider
import com.pluu.support.impl.toUiType
import com.pluu.webtoon.NAV_ITEM
import com.pluu.webtoon.common.PrefConfig
import com.pluu.webtoon.data.db.IDBHelper
import com.pluu.webtoon.data.model.AppDatabase
import com.pluu.webtoon.data.network.INetworkUseCase
import com.pluu.webtoon.data.network.NetworkUseCase
import com.pluu.webtoon.di.init.InitUseCase
import com.pluu.webtoon.utils.AppCoroutineDispatchers
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val dbModule = module {
    single<IDBHelper> {
        AppDatabase.getInstance(androidContext()).roomDao()
    }
}

private val appModule = module {
    single {
        AppCoroutineDispatchers(
            io = Dispatchers.IO,
            computation = Dispatchers.Default,
            main = Dispatchers.Main
        )
    }
    single { PrefConfig(androidContext()) }

    factory {
        InitUseCase(get())
    }
}

private val networkModule = module {
    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }
    single<INetworkUseCase>(named(AppProperties.NETWORK)) {
        NetworkUseCase(get())
    }
}

private val resourceModule = module {
    single { ColorProvider(get()) }
    factory {
        NaviColorProvider(
            get(),
            getProperty<NAV_ITEM>(ServiceProperties.NAV_ITEM).toUiType()
        )
    }
}

val modules = arrayOf(appModule, dbModule, networkModule, resourceModule)

object AppProperties {
    const val NETWORK = "NETWORK"
}
