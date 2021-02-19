package com.pluu.compose.ui.graphics

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap

@Composable
inline fun rememberImageAsset(
    initValues: ImageBitmap? = null,
    init: @DisallowComposableCalls () -> ImageBitmap?
) = remember(initValues) { mutableStateOf(init()) }

@Composable
inline fun rememberDrawable(
    initValues: Drawable? = null,
    init: @DisallowComposableCalls () -> Drawable?
) = remember(initValues) { mutableStateOf(init()) }
