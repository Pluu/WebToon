package com.pluu.compose.ambient

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

val LocalPreference = staticCompositionLocalOf<PreferenceCompose> {
    error("LocalPreferenceProvider value not available. Are you using ProvidePreference?")
}

@Composable
fun ProvidePreference(
    preferences: SharedPreferences,
    content: @Composable () -> Unit,
) {
    val rememberPreferences = remember {
        PreferenceCompose(preferences)
    }
    CompositionLocalProvider(LocalPreference provides rememberPreferences) {
        content()
    }
}

class PreferenceCompose(
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