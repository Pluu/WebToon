package com.pluu.compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun <T> ListDialog(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    onDismissRequest: () -> Unit,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismissRequest,
        properties = properties
    ) {
        ListDialogContent(
            title = title,
            buttons = buttons,
            shape = shape,
            containerColor = containerColor,
            tonalElevation = tonalElevation,
            buttonContentColor = MaterialTheme.colorScheme.primary,
            titleContentColor = titleContentColor,
            items = items,
            key = key,
            itemContent = itemContent
        )
    }
}

@Composable
private fun <T> ListDialogContent(
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    buttons: (@Composable () -> Unit)? = null,
    shape: Shape,
    containerColor: Color,
    tonalElevation: Dp,
    buttonContentColor: Color,
    titleContentColor: Color,
    items: List<T>,
    key: ((index: Int, item: T) -> Any)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    Surface(
        modifier = modifier,
        shape = shape,
        color = containerColor,
        tonalElevation = tonalElevation
    ) {
        Column(
            modifier = Modifier.padding(DialogPadding)
        ) {
            title?.let {
                CompositionLocalProvider(LocalContentColor provides titleContentColor) {
                    val textStyle = MaterialTheme.typography.headlineSmall
                    ProvideTextStyle(textStyle) {
                        Box(
                            Modifier
                                .padding(TitlePadding)
                                .align(Alignment.Start)
                        ) {
                            title()
                        }
                    }
                }
            }
            HorizontalDivider()
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                itemsIndexed(
                    items = items,
                    key = key,
                    itemContent = itemContent
                )
            }
            buttons?.let {
                Spacer(Modifier.height(2.dp))
                Box(modifier = Modifier.align(Alignment.End)) {
                    CompositionLocalProvider(LocalContentColor provides buttonContentColor) {
                        val textStyle = TextStyle.Default.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            lineHeight = 20.sp,
                            letterSpacing = 0.1.sp
                        )
                        ProvideTextStyle(value = textStyle, content = buttons)
                    }
                }
            }
        }
    }
}

private val DialogPadding = PaddingValues(all = 24.dp)
private val TitlePadding = PaddingValues(bottom = 16.dp)

@Preview
@Composable
fun PreviewListDialogContent() {
    val list = (1..3).map {
        "Content $it"
    }
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
        shape = AlertDialogDefaults.shape,
        containerColor = AlertDialogDefaults.containerColor,
        tonalElevation = AlertDialogDefaults.TonalElevation,
        buttonContentColor = MaterialTheme.colorScheme.primary,
        titleContentColor = AlertDialogDefaults.titleContentColor,
        items = list
    ) { index, item ->
        Text(
            text = "$index : $item",
            modifier = Modifier
                .sizeIn(minHeight = 48.dp)
                .wrapContentHeight()
        )
        HorizontalDivider(color = Color.LightGray)
    }
}
