package com.pluu.compose.preference

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.ui.tooling.preview.Preview
import com.pluu.compose.R

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null
) {
    contentPreference(
        modifier = modifier,
        title = title,
        summary = summary
    )
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    asset: VectorAsset? = null,
    title: String,
    summary: String? = null
) {
    contentPreference(
        modifier = modifier,
        title = title,
        summary = summary
    ) {
        if (asset != null) {
            Image(
                asset = asset,
                modifier = ImageSize
            )
        }
    }
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    asset: ImageAsset? = null,
    title: String,
    summary: String? = null
) {
    contentPreference(
        modifier = modifier,
        title = title, summary = summary
    ) {
        if (asset != null) {
            Image(
                asset = asset,
                modifier = ImageSize
            )
        }
    }
}

@Composable
private fun contentPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null,
    imageAsset: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .sizeIn(minHeight = 48.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .wrapContentHeight()
    ) {
        Box(
            Modifier.preferredWidth(52.dp)
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
        ) {
            imageAsset?.invoke()
        }
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 6.dp)
        ) {
            Text(
                text = title,
                maxLines = 1,
                color = MaterialTheme.colors.onSurface,
                style = MaterialTheme.typography.subtitle1,
                fontSize = 16.sp
            )
            if (summary != null) {
                Spacer(Modifier.preferredHeight(2.dp))
                Text(
                    text = summary,
                    color = MaterialTheme.colors.onSurface.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

internal val ImageSize = Modifier.size(26.dp)

@Preview
@Composable
fun previewPreference() {
    Preference(
        asset = vectorResource(id = R.drawable.ic_baseline_android_24),
        title = "Title",
        summary = "Summary"
    )
}