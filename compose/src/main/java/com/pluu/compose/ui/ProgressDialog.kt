package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.material.AmbientContentAlpha
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Providers
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProgressDialog(
    title: String? = null,
    modifier: Modifier = Modifier.padding(12.dp),
    onDismiss: () -> Unit = { },
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties? = null
) {
    dialogContent(
        title = {
            if (title != null) {
                Text(text = title)
            }
        },
        modifier = modifier,
        onDismiss = onDismiss,
        shape = shape,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        properties = properties
    )
}

@Composable
private fun dialogContent(
    title: (@Composable () -> Unit)? = null,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties? = null
) {
    Dialog(onDismissRequest = onDismiss, properties = properties) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            Row {
                CircularProgressIndicator(
                    modifier = modifier.preferredSize(48.dp),
                    color = MaterialTheme.colors.secondary
                )
                if (title != null) {
                    Box(modifier = TitlePadding.align(Alignment.CenterVertically)) {
                        Providers(AmbientContentAlpha provides ContentAlpha.high) {
                            val textStyle = MaterialTheme.typography.subtitle1
                            ProvideTextStyle(textStyle, title)
                        }
                    }
                }
            }
        }
    }
}