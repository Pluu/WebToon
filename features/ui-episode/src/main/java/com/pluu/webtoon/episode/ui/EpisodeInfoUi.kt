package com.pluu.webtoon.episode.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pluu.webtoon.episode.R

@Composable
fun EpisodeInfoUi(
    modifier: Modifier = Modifier,
    name: String,
    rate: Double,
    infoTextColor: Color = MaterialTheme.colors.onSurface,
    buttonBackgroundColor: Color = MaterialTheme.colors.primary,
    onFirstClicked: () -> Unit
) {
    Row(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically)
        ) {
            Text(
                text = name,
                fontSize = 22.sp,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
                color = infoTextColor,
                overflow = TextOverflow.Ellipsis
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
                painter = painterResource(R.drawable.ic_looks_one_white_36),
                tint = Color.White,
                contentDescription = null
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
    showBackground = true, backgroundColor = 0xFFF
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
    showBackground = true, backgroundColor = 0xFFF
)
@Composable
fun PreviewEpisodeInfoEmptyRateUi() {
    EpisodeInfoUi(
        name = "타이틀",
        rate = 0.0,
        onFirstClicked = {}
    )
}