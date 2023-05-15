@file:Suppress("unused")

package com.pluu.webtoon.ui.compose

import android.view.ViewGroup
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import com.pluu.webtoon.ui.compose.theme.AppTheme

fun Fragment.fragmentComposeView(
    parent: CompositionContext? = null,
    content: @Composable () -> Unit
): ComposeView = ComposeView(requireContext()).apply {
    setParentCompositionContext(parent)
    layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )
    setContent {
        content()
    }
}

@Composable
fun WebToonTheme(
    useDarkColors: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(useDarkTheme = useDarkColors, content = content)
}