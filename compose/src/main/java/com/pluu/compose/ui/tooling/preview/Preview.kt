package com.pluu.compose.ui.tooling.preview

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

private const val width = 280
private const val height = 340

@Preview(
    name = "Day Preview",
    widthDp = width
)
@Preview(
    name = "Night Preview",
    widthDp = width,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DayNightWrapPreview

@Preview(
    name = "Day Preview",
    widthDp = width,
    heightDp = height
)
@Preview(
    name = "Night Preview",
    widthDp = width,
    heightDp = height,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
annotation class DayNightPreview