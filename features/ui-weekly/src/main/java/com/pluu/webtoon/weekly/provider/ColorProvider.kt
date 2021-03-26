package com.pluu.webtoon.weekly.provider

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.pluu.webtoon.model.NAV_ITEM
import com.pluu.webtoon.weekly.model.toUiType
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/** Color Resource Provider */
@Singleton
class ColorProvider @Inject constructor(
    @ApplicationContext private val context: Context
) {
    @ColorInt
    fun getColor(@ColorRes resId: Int): Int = ContextCompat.getColor(context, resId)
}

/** Navigation Color Provider */
class NaviColorProvider @Inject constructor(
    private val provider: ColorProvider,
    naviItem: NAV_ITEM
) {
    private val uiNaviItem = naviItem.toUiType()

    @ColorInt
    fun getTitleColor(): Int = provider.getColor(uiNaviItem.color)

    @ColorInt
    fun getTitleColorVariant(): Int = provider.getColor(uiNaviItem.bgColor)
}
