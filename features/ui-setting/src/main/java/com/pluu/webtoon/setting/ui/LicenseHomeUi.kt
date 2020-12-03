package com.pluu.webtoon.setting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel
import dev.chrisbanes.accompanist.insets.statusBarsPadding

@Composable
fun LicenseHomeUi(
    list: List<LicenseModel>,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onClicked: (item: LicenseModel) -> Unit
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(text = "오픈소스 라이센스")
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Filled.ArrowBack)
                }
            },
            modifier = modifier
                .background(MaterialTheme.colors.primarySurface)
                .statusBarsPadding(),
            backgroundColor = MaterialTheme.colors.primarySurface,
            elevation = 0.dp
        )
        LicenseContentUi(
            list = list,
            modifier = Modifier.fillMaxSize(),
            onClicked = onClicked
        )
    }
}

@Composable
private fun LicenseContentUi(
    list: List<LicenseModel>,
    modifier: Modifier = Modifier,
    onClicked: (item: LicenseModel) -> Unit
) {
    LazyColumnFor(
        items = list,
        modifier = modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 3.dp)
    ) { item ->
        LicenseItemUi(
            item = item,
            onClicked = onClicked
        )
    }
}

@Preview(
    widthDp = 320,
    heightDp = 240,
    showBackground = true, backgroundColor = 0xFFFFFFFF
)
@Composable
fun PreviewLicenseHomeUi() {
    LicenseHomeUi(
        list = licenseModels,
        modifier = Modifier.fillMaxSize(),
        onBackPressed = {},
        onClicked = {}
    )
}