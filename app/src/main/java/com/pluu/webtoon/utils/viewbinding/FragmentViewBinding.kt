package com.pluu.webtoon.utils.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(
    bind: (View) -> T
): Lazy<T> = object : Lazy<T> {
    private var chached: T? = null

    override fun isInitialized(): Boolean = chached != null

    override val value: T
        get() = chached ?: bind(requireView()).also {
            chached = it
        }

    init {
        lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                autoDestroyBinding()
            }
        })
    }

    private fun autoDestroyBinding() {
        viewLifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_DESTROY) {
                chached = null
            }
        })
    }
}