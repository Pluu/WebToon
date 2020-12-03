package com.pluu.compose.preference

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.preference.PreferenceManager
import com.pluu.compose.R
import com.pluu.compose.ui.ListDialog

data class ListPreferenceItem<T>(
    val entry: String,
    val entryValue: T
)

@Composable
fun <T> ListPreference(
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    modifier: Modifier = Modifier,
    title: String,
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by remember { mutableStateOf(false) }

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
    )
}

@Composable
fun <T> ListPreference(
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    modifier: Modifier = Modifier,
    title: String,
    imageVector: ImageVector? = null,
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by remember { mutableStateOf(false) }

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
        imageVector = imageVector
    )
}

@Composable
fun <T> ListPreference(
    items: List<ListPreferenceItem<T>>,
    preferenceState: PreferenceState<T>,
    modifier: Modifier = Modifier,
    title: String,
    bitmap: ImageBitmap? = null,
    onSelected: (key: String, item: ListPreferenceItem<T>) -> Unit = { _, _ -> }
) {
    var showDialog by remember { mutableStateOf(false) }

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
        bitmap = bitmap
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
        items = items
    ) { _, item ->
        Text(
            text = item.entry,
            modifier = Modifier.fillMaxWidth()
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

@Preview
@Composable
fun PreviewListPreference() {
    val preferenceState = PreferenceState(
        key = "Test",
        initialValue = "Init value",
        initialSummary = "Init Summary",
        preferences = PreferenceManager.getDefaultSharedPreferences(AmbientContext.current),
        onValueChange = { _, _ -> }
    )

    ListPreference(
        items = (0..20).map {
            ListPreferenceItem(it.toString(), "Context ${it + 1}")
        },
        preferenceState = preferenceState,
        title = "Title",
        imageVector = vectorResource(R.drawable.ic_baseline_android_24)
    )
}
