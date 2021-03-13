package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    onDismiss: () -> Unit = { },
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
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
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
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
                    color = MaterialTheme.colors.secondary
                )
                if (title != null) {
                    Box(modifier = TitlePadding.align(Alignment.CenterVertically)) {
                        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                            val textStyle = MaterialTheme.typography.subtitle1
                            ProvideTextStyle(textStyle, title)
                        }
                    }
                }
            }
        }
    }
}