package com.pluu.webtoon.ui.compose.graphics

import androidx.annotation.ColorLong
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.util.annotation.ColorInt

inline fun @receiver:ColorInt Int.toColor(): Color = Color(this)
inline fun @receiver:ColorLong Long.toColor(): Color = Color(this)
