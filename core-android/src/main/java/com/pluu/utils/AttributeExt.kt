package com.pluu.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

@ColorInt
fun Context.getThemeColor(
    @AttrRes themeAttrId: Int
): Int = obtainStyledAttributes(
    intArrayOf(themeAttrId)
).use {
    it.getColor(0, Color.MAGENTA)
}
