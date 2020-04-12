package com.pluu.support.impl

import android.content.Context
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/** Color Resource Provider */
class ColorProvider(
    private val context: Context
) {
    fun getColor(
        @ColorRes resId: Int
    ): Int = ContextCompat.getColor(context, resId)
}

/** Navigation Color Provider */
class NaviColorProvider(
    private val provider: ColorProvider,
    private val naviItem: UI_NAV_ITEM
) {
    fun getTitleColor(): Int = provider.getColor(naviItem.color)
    fun getTitleColorVariant(): Int = provider.getColor(naviItem.bgColor)
}
