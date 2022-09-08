package com.pluu.webtoon.setting.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.pluu.compose.ui.tooling.preview.DayNightPreview
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel
import com.pluu.webtoon.ui.compose.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LicenseScreen(
    modifier: Modifier = Modifier,
    list: List<LicenseModel>,
    onBackPressed: () -> Unit,
    onClicked: (item: LicenseModel) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("오픈소스 라이센스") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        },
        modifier = modifier.fillMaxWidth()
    ) { innerPadding ->
        LicenseContentUi(
            list = list,
            modifier = Modifier.padding(innerPadding),
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
//            .background(MaterialTheme.colorScheme.surface)
    ) {
        items(list, key = { license -> license.url }) { item ->
            LicenseItemUi(
                item = item,
                onClicked = onClicked
            )
        }
    }
}

@DayNightPreview
@Composable
private fun PreviewLicenseHomeUi() {
    AppTheme {
        LicenseScreen(
            list = licenseModels.subList(0, 2),
            onBackPressed = {},
            onClicked = {}
        )
    }
}
