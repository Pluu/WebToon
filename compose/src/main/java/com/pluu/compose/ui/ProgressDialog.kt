package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    onDismiss: () -> Unit = { },
    shape: Shape = RectangleShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    ListDialogContent(
        title = {
            if (title != null) {
                Text(text = title)
            }
        },
        modifier = modifier.padding(12.dp),
        onDismiss = onDismiss,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
    )
}

@Composable
private fun ListDialogContent(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    onDismiss: () -> Unit,
    shape: Shape = RectangleShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Row {
                CircularProgressIndicator(
                    modifier = modifier.size(48.dp),
                    color = MaterialTheme.colorScheme.secondary
                )
                if (title != null) {
                    Box(modifier = TitlePadding.align(Alignment.CenterVertically)) {
                        title()
                    }
                }
            }
        }
    }
}