package com.pluu.compose.ui.graphics

import androidx.annotation.ColorInt
import androidx.annotation.ColorLong
import androidx.compose.ui.graphics.Color

fun @receiver:ColorInt Int.toColor(): Color = Color(this)
fun @receiver:ColorLong Long.toColor(): Color = Color(this)
