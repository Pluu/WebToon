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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun <T> ListDialog(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    onDismiss: () -> Unit,
    shape: Shape = RectangleShape,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = contentColorFor(backgroundColor),
    properties: DialogProperties = DialogProperties(),
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = properties
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = backgroundColor,
            contentColor = contentColor
        ) {
            ListDialogContent(
                title = title,
                buttons = buttons,
                items = items,
                key = key,
                itemContent = itemContent
            )
        }
    }
}

@Composable
private fun <T> ListDialogContent(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Column(modifier = modifier) {
        if (title != null) {
            Box(modifier = TitlePadding.align(Alignment.Start)) {
                title()
            }
        }
        Divider()
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(
                items = items,
                key = key,
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
        ListDialogContent(
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
