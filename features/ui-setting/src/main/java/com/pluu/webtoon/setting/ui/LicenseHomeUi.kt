package com.pluu.webtoon.setting.ui

import android.content.Context
import androidx.compose.foundation.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ContextAmbient
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.pluu.webtoon.setting.R
import com.pluu.webtoon.setting.licenseModels
import com.pluu.webtoon.setting.model.LicenseModel

@Composable
fun LicenseContentUi(
    context: Context = ContextAmbient.current,
    list: List<LicenseModel>,
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit,
    onClicked: (item: LicenseModel) -> Unit
) {
    Column(modifier = modifier) {
        TopAppBar(
            title = {
                Text(text = context.getString(R.string.label_license))
            },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Filled.ArrowBack)
                }
            }
        )
        LicenseHomeUi(
            list = list,
            modifier = Modifier.fillMaxSize(),
            onClicked = onClicked
        )
    }
}

@Composable
fun LicenseHomeUi(
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
fun previewLicenseContentUi() {
    LicenseContentUi(
        list = licenseModels,
        modifier = Modifier.fillMaxSize(),
        onClicked = {},
        onBackPressed = {}
    )
}