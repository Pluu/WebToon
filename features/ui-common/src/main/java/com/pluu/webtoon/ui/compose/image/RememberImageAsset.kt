package com.pluu.webtoon.ui.compose.image

import android.graphics.drawable.Drawable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposableContract
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageAsset

@Composable
inline fun rememberImageAsset(
    initValues: ImageAsset? = null,
    init: @ComposableContract(preventCapture = true) () -> ImageAsset?
) = remember(initValues) { mutableStateOf(init()) }

@Composable
inline fun rememberDrawable(
    initValues: Drawable? = null,
    init: @ComposableContract(preventCapture = true) () -> Drawable?
) = remember(initValues) { mutableStateOf(init()) }
