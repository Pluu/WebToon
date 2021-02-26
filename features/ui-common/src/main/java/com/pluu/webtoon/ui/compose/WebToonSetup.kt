package com.pluu.webtoon.ui.compose

import android.app.Activity
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.google.android.material.composethemeadapter.MdcTheme

@Suppress("NOTHING_TO_INLINE")
@Composable
inline fun WebToonSetup(noinline content: @Composable () -> Unit) {
    MdcTheme {
        content()
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Activity.activityComposeView(
    noinline content: @Composable () -> Unit
) = ComposeView(this).apply {
    setContentView(this)
    setContent {
        WebToonSetup {
            content()
        }
    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun Fragment.fragmentComposeView(
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
