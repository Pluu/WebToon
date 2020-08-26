package com.pluu.utils

import android.content.Context
import android.graphics.Point
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.hardware.display.DisplayManagerCompat
import androidx.fragment.app.Fragment

fun Context.getCompatColor(@ColorRes resId: Int) = ContextCompat.getColor(this, resId)

fun Context.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(resId), duration)
}

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.toast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(resId), duration)
}

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(context, message, duration).show()
}

fun Context.screenWidth() = screen().first

fun Context.screenHeight() = screen().second

fun Context.screen(): Pair<Int, Int> {
    return DisplayManagerCompat.getInstance(this)
        .displays.firstOrNull()
        ?.let {
            val size = Point()
            it.getRealSize(size)
            size.x to size.y
        } ?: 0 to 0
}
