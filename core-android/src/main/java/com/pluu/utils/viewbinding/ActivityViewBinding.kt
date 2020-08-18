package com.pluu.utils.viewbinding

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding

/** Origin Source : https://github.com/wada811/ViewBinding-ktx */
fun <T : ViewBinding> FragmentActivity.viewBinding(
    bind: (View) -> T
): Lazy<T> = object : Lazy<T> {
    private var cached: T? = null

    override fun isInitialized(): Boolean = cached != null

    override val value: T
        get() = cached ?: bind(getContentView()).also {
            cached = it
        }

    private fun FragmentActivity.getContentView(): View {
        return checkNotNull(findViewById<ViewGroup>(android.R.id.content).getChildAt(0)) {
            "Call setContentView or Use Activity's secondary constructor passing layout res id."
        }
    }
}