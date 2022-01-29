package com.pluu.webtoon.intro.ui

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluu.webtoon.ui.compose.theme.AppTheme

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
                color = MaterialTheme.colorScheme.onBackground,
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
                    color = colorResource(id = com.pluu.webtoon.ui_common.R.color.progress_accent_color),
                    strokeWidth = 4.0.dp * 1.5f
                )
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(
    widthDp = 320, heightDp = 480,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewIntroUi() {
    AppTheme {
        IntroScreen(
            isLoading = true,
            modifier = Modifier.background(MaterialTheme.colorScheme.background)
        )
    }
}
