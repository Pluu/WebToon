package com.pluu.webtoon.intro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun IntroScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = "PluuToon",
            modifier = Modifier.align(Alignment.Center),
            color = Color(0xFF0091EA),
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp
        )

        Column(
            modifier = Modifier.align(alignment = Alignment.BottomCenter)
        ) {
            Text(
                text = if (isLoading) {
                    "준비중이야... 기다려..."
                } else {
                    "다됐어... 이제 갈거야..."
                },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedVisibility(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                visible = isLoading
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(64.dp),
                    color = MaterialTheme.colors.secondary,
                    strokeWidth = ProgressIndicatorDefaults.StrokeWidth * 1.5f
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(
    widthDp = 320,
    heightDp = 640
)
@Composable
private fun PreviewIntroUi() {
    IntroScreen(isLoading = true)
}
