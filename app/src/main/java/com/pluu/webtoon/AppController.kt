package com.pluu.webtoon

import android.app.Application
import com.pluu.webtoon.di.convertModule
import com.pluu.webtoon.di.introModule
import com.pluu.webtoon.di.modules
import com.pluu.webtoon.di.webToonModule
import io.realm.Realm
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

/**
 * Application Controller
 * Created by pluu on 2015-03-17.
 */
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin {
            androidContext(this@AppController)
            modules(modules =
                listOf(
                    *modules,
                    introModule,
                    convertModule,
                    webToonModule
                )
            )
        }
    }
}
