package com.pluu.webtoon.setting.ui

import androidx.compose.foundation.Text
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.setting.model.LicenseModel

@Composable
fun LicenseItemUi(
    item: LicenseModel,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onClicked: (item: LicenseModel) -> Unit
) {
    Text(
        text = item.title,
        modifier = modifier
            .preferredHeight(48.dp)
            .clickable(onClick = { onClicked.invoke(item) })
            .padding(horizontal = 16.dp)
            .wrapContentSize(align = Alignment.CenterStart),
        color = MaterialTheme.colors.onSurface
    )
}

@Preview(
    widthDp = 240,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun previewLicenseItemUi() {
    Box {
        val sample = LicenseModel("Test Title", "")
        LicenseItemUi(item = sample, onClicked = {})
    }
}


