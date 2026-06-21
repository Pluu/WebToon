package com.pluu.webtoon.weekly.ui.day

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.style.contentPadding
import androidx.compose.foundation.style.styleable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.ui.compose.WebToonTheme

@Composable
fun WeeklyStatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
) {
    Text(
        text = text,
        color = Color.White,
        modifier = modifier
            .styleable {
                background(backgroundColor)
                shape(RoundedCornerShape(4.dp))
                contentPadding(horizontal = 6.dp, vertical = 2.dp )
            }
    )
}

@Preview
@Composable
private fun WeeklyStatusBadgePreview() {
    WebToonTheme {
        WeeklyStatusBadge(
            text = "UP",
            backgroundColor = Color.Red
        )
    }
}