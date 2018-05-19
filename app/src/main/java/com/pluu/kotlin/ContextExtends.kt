package com.pluu.kotlin

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.content.systemService
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
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val display = systemService<WindowManager>().defaultDisplay
        val size = Point()
        display.getRealSize(size)
        size.x to size.y
    } else {
        val displayMetrics = resources.displayMetrics
        displayMetrics.widthPixels to displayMetrics.heightPixels
    }
}