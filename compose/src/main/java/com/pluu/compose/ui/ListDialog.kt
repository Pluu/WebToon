package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun <T> ListDialog(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    items: List<T>,
    onDismiss: () -> Unit,
    shape: Shape = MaterialTheme.shapes.medium,
    backgroundColor: Color = MaterialTheme.colors.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            DialogContent(
                title = title,
                buttons = buttons,
                items = items,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun <T> DialogContent(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    items: List<T>,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Column(modifier = modifier) {
        if (title != null) {
            Box(modifier = TitlePadding.align(Alignment.Start)) {
                CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                    val textStyle = MaterialTheme.typography.subtitle1
                    ProvideTextStyle(textStyle, title)
                }
            }
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(
                items = items,
                itemContent = itemContent
            )
        }
        if (buttons != null) {
            Spacer(Modifier.height(2.dp))
            buttons.invoke()
        }
        Spacer(Modifier.height(1.dp))
    }
}

internal val TitlePadding = Modifier.padding(24.dp)

@Preview
@Composable
fun PreviewListDialog() {
    val list = (1..3).map {
        "Content $it"
    }
    Surface(color = Color.White) {
        DialogContent(
            title = {
                Text(text = "Test")
            },
            buttons = {
                Button(onClick = {}) {
                    Text(text = "Button")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            items = list
        ) { index, item ->
            Text(
                text = "$index : $item",
                modifier = Modifier.sizeIn(minHeight = 48.dp).wrapContentHeight()
            )
            Divider(color = Color.LightGray)
        }
    }
}
