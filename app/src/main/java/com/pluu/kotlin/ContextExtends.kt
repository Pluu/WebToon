package com.pluu.kotlin

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.WindowManager
import android.widget.Toast


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
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getRealSize(size)
        size.x to size.y
    } else {
        val displayMetrics = resources.displayMetrics
        displayMetrics.widthPixels to displayMetrics.heightPixels
    }
}

val Context.windowManager: WindowManager
    get() = getSystemService(Context.WINDOW_SERVICE) as WindowManager