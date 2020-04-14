package com.pluu.webtoon

import android.app.Application
import com.pluu.webtoon.di.convertModule
import com.pluu.webtoon.di.init.InitUseCase
import com.pluu.webtoon.di.introModule
import com.pluu.webtoon.di.modules
import com.pluu.webtoon.di.webToonModule
import com.pluu.webtoon.utils.KoinTimber
import com.pluu.webtoon.utils.ThemeHelper
import com.pluu.webtoon.utils.ThemeType
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * Application Controller
 * Created by pluu on 2015-03-17.
 */
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            logger(KoinTimber())
            androidContext(this@AppController)
            modules(
                modules = listOf(
                    *modules,
                    introModule,
                    convertModule,
                    webToonModule
                )
            )
        }

        // Init Theme
        ThemeHelper.applyTheme(ThemeType.DEFAULT)

        // Run Init UseCase
        val initCase: InitUseCase = getKoin().get()
        initCase.init()
    }
}
