package com.pluu.webtoon.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable

object RippleDrawableFactory {
    fun createFromColor(
        pressedColor: Int,
        backgroundDrawable: Drawable
    ): RippleDrawable {
        return RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null)
    }

    private fun getPressedState(
        pressedColor: Int
    ): ColorStateList {
        return ColorStateList(arrayOf(intArrayOf()), intArrayOf(pressedColor))
    }
}
