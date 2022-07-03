package com.pluu.webtoon

import android.app.Application
import com.pluu.utils.ThemeHelper
import com.pluu.utils.ThemeType
import com.pluu.webtoon.utils.log.ComponentLogger
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient
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

    @Inject
    lateinit var okHttpClient: OkHttpClient

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        componentLogger.initialize(this)

        // Init Theme
        ThemeHelper.applyTheme(ThemeType.DEFAULT)
    }
}
