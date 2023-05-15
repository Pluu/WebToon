package com.pluu.compose.foundation

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

fun Modifier.backgroundCorner(
    color: Color,
    size: Dp
): Modifier = this.then(
    background(
        color = color,
        shape = RoundedCornerShape(size)
    )
)
