package com.pluu.kotlin

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.systemService


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
    val dMetrics = DisplayMetrics()

    val windowManager: WindowManager = systemService()
    windowManager.defaultDisplay.getMetrics(dMetrics)

    return dMetrics.widthPixels to dMetrics.heightPixels
}