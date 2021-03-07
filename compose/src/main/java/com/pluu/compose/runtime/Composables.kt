@file:Suppress("NOTHING_TO_INLINE")

package com.pluu.compose.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

@Composable
inline fun <T> rememberMutableStateOf(
    value: T
): MutableState<T> = remember {
    mutableStateOf(value)
}

@Composable
inline fun <T> rememberMutableStateOf(
    key1: Any,
    calculation: @androidx.compose.runtime.DisallowComposableCalls() () -> T
): MutableState<T> = remember(key1) {
    mutableStateOf(calculation())
}

@Composable
inline fun <T> rememberMutableStateListOf(): SnapshotStateList<T> = remember {
    mutableStateListOf()
}
