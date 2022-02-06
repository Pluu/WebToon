package com.pluu.compose.preference

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.pluu.compose.R
import com.pluu.compose.runtime.rememberMutableStateOf
import com.pluu.compose.ui.ListDialog
import com.pluu.webtoon.ui.compose.theme.AppTheme

data class ListPreferenceItem<T>(
    val entry: String,
    val entryValue: T
)

@Composable
fun <T> ListPreference(
    modifier: Modifier = Modifier,
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    title: String,
    painter: Painter? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by rememberMutableStateOf(false)

    if (showDialog) {
        ShowAlertDialog(
            items = items,
            title = title,
            onClicked = { item ->
                preferenceState.updateEntryValue(item.entryValue)
                onSelected(preferenceState.key, item)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Preference(
        modifier = modifier.clickable(onClick = {
            showDialog = true
        }),
        title = title,
        summary = preferenceState.summary,
        painter = painter,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

@Composable
fun <T> ListPreference(
    modifier: Modifier = Modifier,
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    title: String,
    imageVector: ImageVector? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by rememberMutableStateOf(false)

    if (showDialog) {
        ShowAlertDialog(
            items = items,
            title = title,
            onClicked = { item ->
                preferenceState.updateEntryValue(item.entryValue)
                onSelected(preferenceState.key, item)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Preference(
        modifier = modifier.clickable(onClick = {
            showDialog = true
        }),
        title = title,
        summary = preferenceState.summary,
        imageVector = imageVector,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

@Composable
fun <T> ListPreference(
    modifier: Modifier = Modifier,
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    title: String,
    bitmap: ImageBitmap? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by rememberMutableStateOf(false)

    if (showDialog) {
        ShowAlertDialog(
            items = items,
            title = title,
            onClicked = { item ->
                preferenceState.updateEntryValue(item.entryValue)
                onSelected(preferenceState.key, item)
            },
            onDismiss = {
                showDialog = false
            }
        )
    }

    Preference(
        modifier = modifier.clickable(onClick = {
            showDialog = true
        }),
        title = title,
        summary = preferenceState.summary,
        bitmap = bitmap,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    )
}

@Composable
private fun <LPT, T : ListPreferenceItem<LPT>> ShowAlertDialog(
    title: String,
    items: List<T>,
    onClicked: (T) -> Unit,
    onDismiss: () -> Unit
) {
    ListDialog(
        title = {
            Text(text = title)
        },
        onDismiss = onDismiss,
        items = items,
        key = { _, item -> item.entry }
    ) { _, item ->
        Text(
            text = item.entry,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 48.dp)
                .clickable(onClick = {
                    onClicked(item)
                    onDismiss()
                })
                .padding(horizontal = 15.dp, vertical = 6.dp)
                .wrapContentHeight()
        )
    }
}

@Preview(
    name = "Light Theme",
    widthDp = 320
)
@Preview(
    name = "Dark Theme",
    widthDp = 320,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewListPreference() {
    val preferenceState = PreferenceState(
        key = "Test",
        initialValue = "Init value",
        initialSummary = "Init Summary",
        preferences = PreferenceManager.getDefaultSharedPreferences(LocalContext.current),
        onValueChange = { _, _ -> }
    )

    AppTheme {
        ListPreference(
            items = (0..20).map {
                ListPreferenceItem(it.toString(), "Context ${it + 1}")
            },
            preferenceState = preferenceState,
            title = "Title",
            painter = painterResource(R.drawable.ic_baseline_android_24)
        )
    }
}
