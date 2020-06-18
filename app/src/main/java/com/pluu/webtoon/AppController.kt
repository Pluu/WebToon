package com.pluu.webtoon

import android.app.Application
import com.pluu.webtoon.utils.ThemeHelper
import com.pluu.webtoon.utils.ThemeType
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Application Controller
 * Created by pluu on 2015-03-17.
 */
@HiltAndroidApp
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        // Init Theme
        ThemeHelper.applyTheme(ThemeType.DEFAULT)
    }
}
