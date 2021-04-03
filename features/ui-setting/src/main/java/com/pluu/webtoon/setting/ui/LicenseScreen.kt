package com.pluu.webtoon.setting.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel

@Composable
fun LicenseScreen(
    modifier: Modifier = Modifier,
    list: List<LicenseModel>,
    onBackPressed: () -> Unit,
    onClicked: (item: LicenseModel) -> Unit
) {
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                Text(text = "오픈소스 라이센스")
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
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
    modifier: Modifier = Modifier,
    list: List<LicenseModel>,
    onClicked: (item: LicenseModel) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .background(color = MaterialTheme.colors.surface)
            .padding(horizontal = 3.dp)
    ) {
        items(list, key = { license -> license.url }) { item ->
            LicenseItemUi(
                item = item,
                onClicked = onClicked
            )
        }
    }
}

@Preview(widthDp = 340, heightDp = 640)
@Composable
fun PreviewLicenseHomeUi() {
    ProvideWindowInsets {
        LicenseScreen(
            list = licenseModels,
            onBackPressed = {},
            onClicked = {}
        )
    }
}
