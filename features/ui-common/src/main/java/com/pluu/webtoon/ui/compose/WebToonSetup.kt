package com.pluu.webtoon.ui.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
fun WebToonTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    AppTheme(useDarkTheme = isDarkTheme, content = content)
}