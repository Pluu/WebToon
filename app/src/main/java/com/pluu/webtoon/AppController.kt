package com.pluu.webtoon

import android.app.Application
import com.pluu.webtoon.utils.log.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

/**
 * Application Controller
 * Created by pluu on 2015-03-17.
 */
@HiltAndroidApp
class AppController : Application() {

    @Inject
    lateinit var componentLogger: ComponentLogger

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        componentLogger.initialize(this)
    }
}
