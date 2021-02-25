package com.pluu.compose.ambient

import android.content.SharedPreferences
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPreferenceProvider = staticCompositionLocalOf<PreferenceProvider> {
    error("LocalPreferenceProvider value not available. Are you using ProvidePreference?")
}

class PreferenceProvider(
    val preferences: SharedPreferences
) {
    fun getValue(
        key: String,
        defaultValue: Boolean = false
    ) = preferences.getBoolean(key, defaultValue)

    fun getValue(
        key: String,
        defaultValue: String = ""
    ) = preferences.getString(key, defaultValue)!!

    fun getValue(
        key: String,
        defaultValue: Float = 0f
    ) = preferences.getFloat(key, defaultValue)

    fun getValue(
        key: String,
        defaultValue: Int = 0
    ) = preferences.getInt(key, defaultValue)

    fun getValue(
        key: String,
        defaultValue: Long = 0L
    ) = preferences.getLong(key, defaultValue)

    fun getValue(
        key: String,
        defaultValue: Set<String> = emptySet()
    ): Set<String> = preferences.getStringSet(key, defaultValue)!!
}