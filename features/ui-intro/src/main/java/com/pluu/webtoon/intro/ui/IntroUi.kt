package com.pluu.webtoon.intro.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay

@Composable
internal fun IntroUi(
    modifier: Modifier = Modifier,
    viewModel: IntroViewModel,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    onNavigateToMain: () -> Unit
) {
    val isNextMove by viewModel.observe.collectAsStateWithLifecycle(initialValue = false)

    IntroScreen(
        modifier = modifier
            .background(backgroundColor)
            .statusBarsPadding()
            .navigationBarsPadding(),
        isLoading = !isNextMove
    )

    if (isNextMove) {
        LaunchedEffect(true) {
            delay(500L)
            onNavigateToMain()
        }
    }
}