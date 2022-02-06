package com.pluu.webtoon.episode.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pluu.webtoon.episode.R
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun EpisodeInfoUi(
    modifier: Modifier = Modifier,
    name: String,
    rate: Double,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onFirstClicked: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor
        ) {
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
                    overflow = TextOverflow.Ellipsis
                )
                if (rate != 0.0) {
                    Text(
                        text = "평점 : %.2f".format(rate),
                        maxLines = 1,
                        fontSize = 14.sp,
                    )
                }
            }
        }
        OutlinedButton(
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = contentColor
            ),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                brush = SolidColor(contentColor)
            ),
            onClick = onFirstClicked
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_looks_one_white_36),
                contentDescription = null
            )
            Text(
                text = "첫화보기",
                fontSize = 14.sp,
                maxLines = 1,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(
    name = "Light Theme",
    widthDp = 320
)
@Preview(
    name = "Dark Theme",
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewEpisodeInfoUi() {
    AppTheme {
        EpisodeInfoUi(
            name = "타이틀",
            rate = 1.1,
            onFirstClicked = {}
        )
    }
}
