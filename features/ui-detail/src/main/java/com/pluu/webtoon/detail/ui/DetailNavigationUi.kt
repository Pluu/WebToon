package com.pluu.webtoon.detail.ui

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun DetailNavigationUi(
    modifier: Modifier = Modifier,
    buttonBgColor: Color = MaterialTheme.colorScheme.primary,
    isPrevEnabled: Boolean,
    onPrevClicked: () -> Unit,
    isNextEnabled: Boolean,
    onNextClicked: () -> Unit
) {
    var buttonEnabled by rememberMutableStateOf(false to false)

    Row(modifier = modifier) {
        BottomNavigationButton(
            modifier = Modifier.weight(1f),
            buttonBgColor = buttonBgColor,
            isEnable = isPrevEnabled,
            onClicked = onPrevClicked
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowLeft,
                tint = Color.White,
                contentDescription = null
            )
            Text(
                text = "이전 화",
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth()
            )
        }
        BottomNavigationButton(
            modifier = Modifier.weight(1f),
            buttonBgColor = buttonBgColor,
            isEnable = isNextEnabled,
            onClicked = onNextClicked
        ) {
            Text(
                text = "다음 화",
                color = Color.White,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth()
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                tint = Color.White,
                contentDescription = null
            )
        }
    }

    DisposableEffect(buttonEnabled) {
        buttonEnabled = isPrevEnabled to isNextEnabled
        onDispose { }
    }
}

@Composable
private fun BottomNavigationButton(
    modifier: Modifier,
    buttonBgColor: Color,
    isEnable: Boolean,
    onClicked: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier.fillMaxHeight(),
        colors = ButtonDefaults.buttonColors(
            containerColor = buttonBgColor,
            disabledContainerColor = Color.Black
        ),
        shape = RoundedCornerShape(0.dp),
        enabled = isEnable,
        onClick = onClicked,
        content = content
    )
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDetailNavigationUi() {
    AppTheme {
        DetailNavigationUi(
            modifier = Modifier.height(48.dp),
            buttonBgColor = Color.Red,
            isPrevEnabled = false,
            onPrevClicked = {},
            isNextEnabled = true,
            onNextClicked = {}
        )
    }
}
