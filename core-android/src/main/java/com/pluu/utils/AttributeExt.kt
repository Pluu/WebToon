package com.pluu.utils

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use
import androidx.fragment.app.Fragment

fun Context.resolveAttribute(@AttrRes attributeResId: Int): TypedValue {
    val typedValue = TypedValue()
    if (theme.resolveAttribute(attributeResId, typedValue, true)) {
        return typedValue
    }
    throw IllegalArgumentException(resources.getResourceName(attributeResId))
}

@ColorInt
fun Context.getThemeColor(
    @AttrRes themeAttrId: Int
): Int = obtainStyledAttributes(
    intArrayOf(themeAttrId)
).use {
    it.getColor(0, Color.MAGENTA)
}

fun Fragment.resolveAttribute(@AttrRes attributeResId: Int): TypedValue {
    return requireContext().resolveAttribute(attributeResId)
}
