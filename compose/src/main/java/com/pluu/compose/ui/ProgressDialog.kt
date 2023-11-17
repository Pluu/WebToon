package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

@Composable
fun ProgressDialog(
    modifier: Modifier = Modifier,
    title: String? = null,
    onDismissRequest: () -> Unit = {},
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        ProgressDialogContent(
            title = title?.let {
                { Text(text = it) }
            },
            modifier = modifier.padding(12.dp),
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            titleContentColor = titleContentColor
        )
    }
}

@Composable
private fun ProgressDialogContent(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    shape: Shape = RectangleShape,
    containerColor: Color,
    tonalElevation: Dp,
    titleContentColor: Color,
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation
    ) {
        Row(
            modifier = Modifier.padding(DialogPadding)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(48.dp),
                color = MaterialTheme.colorScheme.secondary
            )
            title?.let {
                Spacer(Modifier.width(24.dp))
                CompositionLocalProvider(LocalContentColor provides titleContentColor) {
                    val textStyle = MaterialTheme.typography.bodyMedium
                    ProvideTextStyle(textStyle) {
                        Box(
                            modifier = Modifier.align(Alignment.CenterVertically)
                        ) {
                            title()
                        }
                    }
                }
            }
        }
    }
}

private val DialogPadding = PaddingValues(all = 24.dp)

@Preview
@Composable
fun PreviewProgressDialogContent() {
    ProgressDialogContent(
        title = {
            Text(text = "Test", color = Color.Black)
        },
        modifier = Modifier.fillMaxWidth(),
        shape = AlertDialogDefaults.shape,
        containerColor = AlertDialogDefaults.containerColor,
        tonalElevation = AlertDialogDefaults.TonalElevation,
        titleContentColor = AlertDialogDefaults.titleContentColor,
    )
}
