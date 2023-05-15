package com.pluu.compose.preference

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.edit

class PreferenceState<T>(
    val key: String,
    initialValue: T,
    initialSummary: String,
    private val preferences: SharedPreferences,
    private val onValueChange: (receiver: PreferenceState<T>, item: T) -> Unit
) {
    private var entryValue by mutableStateOf(initialValue)
    var summary: String by mutableStateOf(initialSummary)

    fun updateEntryValue(value: T) {
        preferences.edit {
            putString(key, value.toString())
        }
        entryValue = value
        onValueChange(this, value)
    }
}

@Composable
fun <T> rememberPreferenceState(
    key: String,
    preferences: SharedPreferences,
    initialValue: T,
    initialSummary: String = "",
    onValueChange: (receiver: PreferenceState<T>, item: T) -> Unit
): PreferenceState<T> = PreferenceState(
    key = key,
    preferences = preferences,
    initialValue = initialValue,
    initialSummary = initialSummary,
    onValueChange = onValueChange
)