package com.pluu.utils.viewbinding

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.viewbinding.ViewBinding

fun <T : ViewBinding> Fragment.viewBinding(
    bind: (View) -> T
): Lazy<T> = object : Lazy<T> {
    private var cached: T? = null

    override fun isInitialized(): Boolean = cached != null

    override val value: T
        get() = cached ?: bind(requireView()).also {
            cached = it
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
                cached = null
            }
        })
    }
}