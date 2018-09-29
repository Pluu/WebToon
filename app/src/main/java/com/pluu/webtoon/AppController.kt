package com.pluu.webtoon

import android.app.Application
import com.pluu.webtoon.di.dbModule
import com.pluu.webtoon.di.networkModule
import com.pluu.webtoon.di.webToonModule
import io.realm.Realm
import org.koin.android.ext.android.startKoin

/**
 * Application Controller
 * Created by pluu on 2015-03-17.
 */
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        startKoin(
            this, listOf(
                dbModule,
                networkModule,
                webToonModule
            )
        )
    }
}
