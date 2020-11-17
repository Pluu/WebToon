package com.pluu.webtoon.ui.intro

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorConstants
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.drawLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.pluu.compose.ui.graphics.toColor

@Composable
fun IntroUi(isNextMove: Boolean) {
    Box(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "PluuToon",
            modifier = Modifier.align(Alignment.Center),
            color = 0xFF0091EA.toColor(),
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp
        )

        Column(
            modifier = Modifier.align(alignment = Alignment.BottomCenter)
        ) {
            Text(
                text = if (isNextMove) {
                    "다됐어... 이제 갈거야..."
                } else {
                    "준비중이야... 기다려..."
                },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
                    .preferredSize(64.dp)
                    .drawLayer(
                        alpha = if (isNextMove) {
                            0f
                        } else {
                            1f
                        }
                    ),
                color = MaterialTheme.colors.secondary,
                strokeWidth = ProgressIndicatorConstants.DefaultStrokeWidth * 1.5f
            )

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(
    widthDp = 320,
    heightDp = 640
)
@Composable
fun previewIntroUi() {
    IntroUi(false)
}
