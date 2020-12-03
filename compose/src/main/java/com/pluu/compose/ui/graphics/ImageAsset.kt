package com.pluu.compose.ui.graphics

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableContract
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Composable
inline fun rememberImageAsset(
    initValues: ImageBitmap? = null,
    init: @ComposableContract(preventCapture = true) () -> ImageBitmap?
) = remember(initValues) { mutableStateOf(init()) }

@Composable
inline fun rememberDrawable(
    initValues: Drawable? = null,
    init: @ComposableContract(preventCapture = true) () -> Drawable?
) = remember(initValues) { mutableStateOf(init()) }
