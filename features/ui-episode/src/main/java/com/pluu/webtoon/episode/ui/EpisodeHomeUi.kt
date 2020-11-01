package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.episode.R

@Composable
fun EpisodeContentUi(
    modifier: Modifier = Modifier,
) {
}

@Composable
fun EpisodeInfoUi(
    name: String,
    rate: Double,
    infoTextColor: Color = MaterialTheme.colors.onSurface,
    buttonBackgroundColor: Color = MaterialTheme.colors.primary,
    modifier: Modifier = Modifier,
    onFirstClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .sizeIn(minHeight = 48.dp)
            .padding(3.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = name,
                fontSize = 22.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = infoTextColor
            )
            if (rate != 0.0) {
                Text(
                    text = "평점 : %.2f".format(rate),
                    maxLines = 1,
                    fontSize = 14.sp,
                    color = infoTextColor
                )
            }
        }
        Button(
            modifier = Modifier.weight(1f),
            colors = ButtonConstants.defaultButtonColors(backgroundColor = buttonBackgroundColor),
            onClick = onFirstClicked
        ) {
            Icon(
                asset = vectorResource(R.drawable.ic_looks_one_white_36),
                tint = Color.White
            )
            Text(
                text = "첫화보기",
                fontSize = 14.sp,
                maxLines = 1,
                color = Color.White,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    widthDp = 320,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun previewEpisodeInfoUi() {
    EpisodeInfoUi(
        name = "타이틀",
        rate = 1.1,
        onFirstClicked = {}
    )
}

@Preview(
    widthDp = 320,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun previewEpisodeInfoEmptyRateUi() {
    EpisodeInfoUi(
        name = "타이틀",
        rate = 0.0,
        onFirstClicked = {}
    )
}