package com.pluu.webtoon.setting.ui

import androidx.compose.runtime.Composable
import com.pluu.webtoon.setting.licenseModels

@Composable
fun LicenseUi(
    closeCurrent: () -> Unit,
    openBrowser: (url: String) -> Unit
) {
    LicenseScreen(
        list = licenseModels,
        onBackPressed = { closeCurrent() },
        onClicked = { openBrowser(it.url) }
    )
}