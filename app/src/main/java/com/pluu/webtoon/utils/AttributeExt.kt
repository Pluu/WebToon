package com.pluu.webtoon.utils

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.fragment.app.Fragment

fun Context.resolveAttribute(@AttrRes attributeResId: Int): TypedValue {
    val typedValue = TypedValue()
    if (theme.resolveAttribute(attributeResId, typedValue, true)) {
        return typedValue
    }
    throw IllegalArgumentException(resources.getResourceName(attributeResId))
}

fun Fragment.resolveAttribute(@AttrRes attributeResId: Int): TypedValue {
    return requireContext().resolveAttribute(attributeResId)
}
