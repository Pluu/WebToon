package com.pluu.webtoon.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate

enum class ThemeType {
    LIGHT, DARK, DEFAULT
}

object ThemeHelper {
    fun applyTheme(type: ThemeType) {
        val mode = when (type) {
            ThemeType.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeType.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            else -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                } else {
                    AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun isDarkTheme(context: Context) = context.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES

    fun isLightTheme(context: Context) = !isDarkTheme(context)
}
