package com.pluu.ui.state

import com.pluu.webtoon.model.Result

///////////////////////////////////////////////////////////////////////////
// Origin : https://github.com/android/compose-samples/blob/main/JetNews/app/src/main/java/com/example/jetnews/ui/state/UiState.kt
///////////////////////////////////////////////////////////////////////////

/**
 * Immutable data class that allows for loading, data, and exception to be managed independently.
 *
 * This is useful for screens that want to show the last successful result while loading or a later
 * refresh has caused an error.
 */
data class UiState<T>(
    val loading: Boolean = false,
    val exception: Throwable? = null,
    val data: T? = null
) {
    /**
     * True if this contains an error
     */
    val hasError: Boolean
        get() = exception != null

    /**
     * True if this represents a first load
     */
    val initialLoad: Boolean
        get() = data == null && loading && !hasError
}

/**
 * Copy a UiState<T> based on a Result<T>.
 *
 * Result.Success will set all fields
 * Result.Error will reset loading and exception only
 */
fun <T> UiState<T>.copyWithResult(value: Result<T>): UiState<T> {
    return when (value) {
        is Result.Success -> copy(loading = false, exception = null, data = value.data)
        is Result.Error -> copy(loading = false, exception = value.throwable)
    }
}
