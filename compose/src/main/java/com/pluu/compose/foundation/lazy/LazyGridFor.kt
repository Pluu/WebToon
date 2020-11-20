package com.pluu.compose.foundation.lazy

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumnForIndexed
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

///////////////////////////////////////////////////////////////////////////
// Origin : https://proandroiddev.com/android-jetpack-compose-exploring-state-based-ui-e1d970471d0a
///////////////////////////////////////////////////////////////////////////

@Composable
fun <T> LazyGridFor(
    items: List<T> = listOf(),
    rows: Int = 2,
    modifier: Modifier = Modifier,
    itemContent: @Composable LazyItemScope.(T, Int) -> Unit
) {
    val chunkedList = items.chunked(rows)
    LazyColumnForIndexed(
        items = chunkedList,
        modifier = modifier
    ) { index, it ->
        Row {
            it.forEachIndexed { rowIndex, item ->
                Box(
                    modifier = Modifier.weight(1F).align(Alignment.Top),
                    alignment = Alignment.Center
                ) {
                    itemContent(item, index * rows + rowIndex)
                }
            }
            repeat(rows - it.size) {
                Box(modifier = Modifier.weight(1F).padding(8.dp)) {}
            }
        }
    }
}