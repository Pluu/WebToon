///////////////////////////////////////////////////////////////////////////
// Origin : https://medium.com/@rgontier/measuring-jetpack-composables-in-preview-c63173179ef9
///////////////////////////////////////////////////////////////////////////

package com.pluu.compose.ui.tooling.preview

import android.content.res.Resources
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * ```kotlin
 * LayoutInspectorComposable(
 *    gridType = GridType.FixedSize(50.dp),
 *    contentColor = Color.White
 *    content = content
 * )
 * ```
 *
 * ```kotlin
 * LayoutInspectorComposable(
 *    gridType = GridType.GridCount(6.dp),
 *    contentColor = Color.White
 *    content = content
 * )
 * ```
 *
 * @param gridType
 * @param contentColor
 * @param content
 */
@Composable
fun LayoutInspectorComposable(
    gridType: GridType = GridType.GridCount(6),
    contentColor: Color = MaterialTheme.colorScheme.onBackground,
    content: @Composable () -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }
    var height by remember { mutableStateOf(0.dp) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .padding(top = 48.dp)
                .onGloballyPositioned { coordinates ->
                    width = coordinates.size.width.toDp()
                    height = coordinates.size.height.toDp()
                }
        ) {
            content()

            // Overlay to draw on top of the content
            Canvas(modifier = Modifier.matchParentSize()) {
                val dashArray = floatArrayOf(10f, 10f)
                PathEffect.dashPathEffect(dashArray, 0f)
                val dashLength = 10f
                val gapLength = 10f

                val gridLines = when (gridType) {
                    is GridType.FixedSize -> {
                        (size.width / gridType.width.toPx()).roundToInt()
                    }

                    is GridType.GridCount -> gridType.count
                }

                val (stepX, stepY) = when (gridType) {
                    is GridType.FixedSize -> {
                        val size = gridType.width.toPx()
                        size to size
                    }

                    is GridType.GridCount -> {
                        size.width / gridLines to size.height / gridLines
                    }
                }
                val alpha = 0.3f

                // Draw vertical grid lines
                for (i in 0..gridLines) {
                    val x = i * stepX
                    var currentY = 0f
                    while (currentY < size.height) {
                        drawLine(
                            color = Color.White.copy(alpha = alpha),
                            start = Offset(x, currentY),
                            end = Offset(x, currentY + dashLength),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            color = Color.Black.copy(alpha = alpha),
                            start = Offset(x, currentY + dashLength),
                            end = Offset(x, currentY + dashLength + gapLength),
                            strokeWidth = 1.dp.toPx()
                        )
                        currentY += dashLength + gapLength
                    }
                }

                // Draw horizontal grid lines
                for (i in 0..gridLines) {
                    val y = i * stepY
                    var currentX = 0f
                    while (currentX < size.width) {
                        drawLine(
                            color = Color.White.copy(alpha = alpha),
                            start = Offset(currentX, y),
                            end = Offset(currentX + dashLength, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        drawLine(
                            color = Color.Black.copy(alpha = alpha),
                            start = Offset(currentX + dashLength, y),
                            end = Offset(currentX + dashLength + gapLength, y),
                            strokeWidth = 1.dp.toPx()
                        )
                        currentX += dashLength + gapLength
                    }
                }
            }
        }

        BasicText(
            text = "w=${width.value.roundToInt()}dp, h=${height.value.roundToInt()}dp\n$gridType",
            modifier = Modifier.align(Alignment.TopStart),
            color = { contentColor }
        )
    }
}

sealed class GridType {
    data class GridCount(val count: Int) : GridType()
    data class FixedSize(val width: Dp) : GridType()
}

fun Int.toDp(): Dp {
    return (this / Resources.getSystem().displayMetrics.density).dp
}