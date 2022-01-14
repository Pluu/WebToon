package com.pluu.compose.ui.res

import androidx.annotation.AttrRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext

@Composable
@ReadOnlyComposable
fun colorAttribute(
    @AttrRes themeAttrId: Int
): Int {
    val context = LocalContext.current
    return context.obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, android.graphics.Color.MAGENTA)
    }
}