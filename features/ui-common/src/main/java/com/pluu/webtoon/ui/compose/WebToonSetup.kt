package com.pluu.webtoon.ui.compose

import android.view.ViewGroup
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun WebToonSetup(noinline content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.FragmentComposeView(
    noinline content: @Composable () -> Unit
) = ComposeView(requireContext()).apply {
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent {
        WebToonSetup {
            content()
        }
    }
}