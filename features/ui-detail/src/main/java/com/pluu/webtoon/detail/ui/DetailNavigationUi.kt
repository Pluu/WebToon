package com.pluu.webtoon.detail.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonConstants
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview

@Composable
fun DetailNavigationUi(
    modifier: Modifier = Modifier,
    buttonBackgroundColor: Color = MaterialTheme.colors.primary,
    isPrevEnabled: Boolean,
    onPrevClicked: () -> Unit,
    isNextEnabled: Boolean,
    onNextClicked: () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Button(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = ButtonConstants.defaultButtonColors(buttonBackgroundColor),
            shape = RoundedCornerShape(0.dp),
            enabled = isPrevEnabled,
            onClick = onPrevClicked
        ) {
            Icon(
                asset = Icons.Default.KeyboardArrowLeft,
                tint = Color.White
            )
            Text(
                text = "이전 화", color = Color.White,
                modifier = Modifier.weight(1f).wrapContentWidth()
            )
        }
        Button(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            colors = ButtonConstants.defaultButtonColors(buttonBackgroundColor),
            shape = RoundedCornerShape(0.dp),
            enabled = isNextEnabled,
            onClick = onNextClicked
        ) {
            Text(
                text = "다음 화",
                color = Color.White,
                modifier = Modifier.weight(1f).wrapContentWidth()
            )
            Icon(
                asset = Icons.Default.KeyboardArrowRight,
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
fun previewDetailNavigationUi() {
    DetailNavigationUi(
        modifier = Modifier.preferredHeight(48.dp),
        isPrevEnabled = true,
        onPrevClicked = {},
        isNextEnabled = true,
        onNextClicked = {}
    )
}
