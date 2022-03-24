package com.pluu.webtoon.setting.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            SmallTopAppBar(
                title = { Text("오픈소스 라이센스") },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .statusBarsPadding(),
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
        item {
            Divider(modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Preview(
    widthDp = 340, heightDp = 640,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun PreviewLicenseHomeUi() {
    AppTheme {
        LicenseScreen(
            list = licenseModels,
            onBackPressed = {},
            onClicked = {}
        )
    }
}
