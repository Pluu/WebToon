package com.pluu.utils

import com.pluu.ui.state.UiState
import com.pluu.webtoon.model.Result

fun <T, R> Result<T>.toUiState(
    mapper: (T) -> UiState<R>
): UiState<R> {
    return when (this) {
        is Result.Success -> mapper(data)
        is Result.Error -> UiState(exception = throwable)
    }
}