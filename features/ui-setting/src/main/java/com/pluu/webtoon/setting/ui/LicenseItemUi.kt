package com.pluu.webtoon.setting.ui

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.setting.model.LicenseModel
import com.pluu.webtoon.ui.compose.theme.AppTheme

@Composable
internal fun LicenseItemUi(
    modifier: Modifier = Modifier,
    item: LicenseModel,
    onClicked: (item: LicenseModel) -> Unit
) {
    Text(
        text = item.title,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clickable(onClick = { onClicked.invoke(item) })
            .padding(horizontal = 16.dp)
            .wrapContentSize(align = Alignment.CenterStart),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Preview(
    widthDp = 240,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewLicenseItemUi() {
    AppTheme {
        Box {
            val sample = LicenseModel("Test Title", "")
            LicenseItemUi(item = sample, onClicked = {})
        }
    }
}


