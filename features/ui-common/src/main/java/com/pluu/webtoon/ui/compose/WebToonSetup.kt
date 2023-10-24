@file:Suppress("unused")

package com.pluu.webtoon.ui.compose

import android.graphics.Color
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
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
fun ComponentActivity.WebToonTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    statusBarStyle: SystemBarStyle = SystemBarStyle.auto(
        Color.TRANSPARENT,
        Color.TRANSPARENT
    ),
    content: @Composable () -> Unit
) {
    DisposableEffect(isDarkTheme) {
        enableEdgeToEdge(statusBarStyle = statusBarStyle)
        onDispose { }
    }

    AppTheme(useDarkTheme = isDarkTheme, content = content)
}