package com.pluu.compose.preference

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pluu.compose.R
import com.pluu.compose.preference.tokens.PreferenceTokens
import com.pluu.compose.ui.tooling.preview.DayNightWrapPreview

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    ContentPreference(
        modifier = modifier,
        title = title,
        summary = summary,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = null,
    title: String,
    summary: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    ContentPreference(
        modifier = modifier,
        title = title,
        summary = summary,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) {
        if (imageVector != null) {
            Image(
                imageVector = imageVector,
                modifier = Modifier.size(PreferenceTokens.ImageSize),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    bitmap: ImageBitmap? = null,
    title: String,
    summary: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    ContentPreference(
        modifier = modifier,
        title = title,
        summary = summary,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) {
        if (bitmap != null) {
            Image(
                bitmap = bitmap,
                modifier = Modifier.size(PreferenceTokens.ImageSize),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Preference(
    modifier: Modifier = Modifier,
    painter: Painter? = null,
    title: String,
    summary: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor)
) {
    ContentPreference(
        modifier = modifier,
        title = title,
        summary = summary,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) {
        if (painter != null) {
            Image(
                painter = painter,
                modifier = Modifier.size(PreferenceTokens.ImageSize),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun ContentPreference(
    modifier: Modifier = Modifier,
    title: String,
    summary: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    content: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .background(backgroundColor)
            .sizeIn(minHeight = PreferenceTokens.PreferenceMinHeight)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .width(PreferenceTokens.LeftImageContainerSize)
                .wrapContentHeight()
        ) {
            content?.invoke()
        }
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = title,
                maxLines = 1,
                color = contentColor,
                style = MaterialTheme.typography.titleMedium,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
            if (summary != null) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = summary,
                    color = contentColor.copy(alpha = 0.75f),
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}

@DayNightWrapPreview
@Composable
fun PreviewPreference() {
    Column {
        Preference(
            title = "Title"
        )
        Divider(color = Color.Gray)
        Preference(
            painter = painterResource(R.drawable.ic_baseline_android_24),
            title = "Title blablabla blablabla blablabla blablabla",
            summary = "Summary blablabla blablabla"
        )
    }
}
