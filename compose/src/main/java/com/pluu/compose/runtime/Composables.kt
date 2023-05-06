@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
inline fun <T> rememberMutableStateOf(
    value: T
): MutableState<T> = remember {
    mutableStateOf(value)
}