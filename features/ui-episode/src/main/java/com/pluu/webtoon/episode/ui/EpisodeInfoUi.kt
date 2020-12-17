package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluu.webtoon.episode.R
import dev.chrisbanes.accompanist.insets.navigationBarsPadding

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
            .navigationBarsPadding()
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
            colors = ButtonDefaults.buttonColors(buttonBackgroundColor),
            onClick = onFirstClicked
        ) {
            Icon(
                imageVector = vectorResource(R.drawable.ic_looks_one_white_36),
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
fun PreviewEpisodeInfoUi() {
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
fun PreviewEpisodeInfoEmptyRateUi() {
    EpisodeInfoUi(
        name = "타이틀",
        rate = 0.0,
        onFirstClicked = {}
    )
}