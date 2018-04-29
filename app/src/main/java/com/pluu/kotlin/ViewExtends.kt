package com.pluu.kotlin

import android.os.Build
import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver

fun Boolean.toVisibleOrGone() = if (this) View.VISIBLE else View.GONE

@Suppress("UNCHECKED_CAST")
fun <T : View> ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = true): T =
    LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot) as T

/**
 * Performs the given action when the view tree is global layout state or the visibility of views.
 */
inline fun View.doOnGlobalLayout(crossinline action: (view: View) -> Unit) {
    val vto = viewTreeObserver
    vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            action(this@doOnGlobalLayout)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                vto.removeOnGlobalLayoutListener(this)
            } else {
                vto.removeGlobalOnLayoutListener(this)
            }
        }
    })
}