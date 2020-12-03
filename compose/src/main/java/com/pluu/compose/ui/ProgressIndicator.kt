/*
 * Copyright 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pluu.compose.ui

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationConstants.Infinite
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.IntPropKey
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredSize
import androidx.compose.foundation.progressSemantics
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProgressIndicatorConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pluu.compose.ui.graphics.toColor
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.max

///////////////////////////////////////////////////////////////////////////
// Origin : https://cs.android.com/androidx/platform/frameworks/support/+/androidx-master-dev:compose/material/material/src/commonMain/kotlin/androidx/compose/material/ProgressIndicator.kt
///////////////////////////////////////////////////////////////////////////

/**
 * An indeterminate circular progress indicator that represents continual progress without a defined
 * start or end point.
 *
 * @param colors The color of the progress indicator.
 * @param strokeWidth The stroke width for the progress indicator.
 */
@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    colors: List<Color> = listOf(MaterialTheme.colors.primary),
    strokeWidth: Dp = ProgressIndicatorConstants.DefaultStrokeWidth
) {
    val stroke = with(AmbientDensity.current) {
        Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Square)
    }
    val state = transition(
        definition = CircularIndeterminateTransition,
        initState = 0,
        toState = 1
    )

    // Select color Index
    var prevAngleOffset = 0f
    var colorIndex = 0

    Canvas(
        modifier
            .progressSemantics()
            .preferredSize(CircularIndicatorDiameter)
    ) {
        val currentRotation = state[IterationProp]
        val baseRotation = state[BaseRotationProp]

        val currentRotationAngleOffset = (currentRotation * RotationAngleOffset) % 360f

        var startAngle = state[TailRotationProp]
        val endAngle = state[HeadRotationProp]
        // How long a line to draw using the start angle as a reference point
        val sweep = abs(endAngle - startAngle)

        // Offset by the constant offset and the per rotation offset
        startAngle += StartAngleOffset + currentRotationAngleOffset
        startAngle += baseRotation

        if (colors.size > 1 && prevAngleOffset != currentRotationAngleOffset) {
            colorIndex = (colorIndex + 1) % colors.size
            prevAngleOffset = currentRotationAngleOffset
        }

        drawIndeterminateCircularIndicator(
            startAngle,
            strokeWidth,
            sweep,
            colors[colorIndex],
            stroke
        )
    }
}

private fun DrawScope.drawIndeterminateCircularIndicator(
    startAngle: Float,
    strokeWidth: Dp,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // Length of arc is angle * radius
    // Angle (radians) is length / radius
    // The length should be the same as the stroke width for calculating the min angle
    val squareStrokeCapOffset =
        (180.0 / PI).toFloat() * (strokeWidth / (CircularIndicatorDiameter / 2)) / 2f

    // Adding a square stroke cap draws half the stroke width behind the start point, so we want to
    // move it forward by that amount so the arc visually appears in the correct place
    val adjustedStartAngle = startAngle + squareStrokeCapOffset

    // When the start and end angles are in the same place, we still want to draw a small sweep, so
    // the stroke caps get added on both ends and we draw the correct minimum length arc
    val adjustedSweep = max(sweep, 0.1f)

    drawCircularIndicator(adjustedStartAngle, adjustedSweep, color, stroke)
}

private fun DrawScope.drawCircularIndicator(
    startAngle: Float,
    sweep: Float,
    color: Color,
    stroke: Stroke
) {
    // To draw this circle we need a rect with edges that line up with the midpoint of the stroke.
    // To do this we need to remove half the stroke width from the total diameter for both sides.
    val diameterOffset = stroke.width / 2
    val arcDimen = size.width - 2 * diameterOffset
    drawArc(
        color = color,
        startAngle = startAngle,
        sweepAngle = sweep,
        useCenter = false,
        topLeft = Offset(diameterOffset, diameterOffset),
        size = Size(arcDimen, arcDimen),
        style = stroke
    )
}

// CircularProgressIndicator Material specs
// Diameter of the indicator circle
private val CircularIndicatorDiameter = 40.dp

// Indeterminate circular indicator transition specs

// The animation comprises of 5 rotations around the circle forming a 5 pointed star.
// After the 5th rotation, we are back at the beginning of the circle.
private const val RotationsPerCycle = 5

// Each rotation is 1 and 1/3 seconds, but 1332ms divides more evenly
private const val RotationDuration = 1332

// When the rotation is at its beginning (0 or 360 degrees) we want it to be drawn at 12 o clock,
// which means 270 degrees when drawing.
private const val StartAngleOffset = -90f

// How far the base point moves around the circle
private const val BaseRotationAngle = 286f

// How far the head and tail should jump forward during one rotation past the base point
private const val JumpRotationAngle = 290f

// Each rotation we want to offset the start position by this much, so we continue where
// the previous rotation ended. This is the maximum angle covered during one rotation.
private const val RotationAngleOffset = (BaseRotationAngle + JumpRotationAngle) % 360f

// The head animates for the first half of a rotation, then is static for the second half
// The tail is static for the first half and then animates for the second half
private const val HeadAndTailAnimationDuration = (RotationDuration * 0.5).toInt()
private const val HeadAndTailDelayDuration = HeadAndTailAnimationDuration

// The current rotation around the circle, so we know where to start the rotation from
private val IterationProp = IntPropKey()

// How far forward (degrees) the base point should be from the start point
private val BaseRotationProp = FloatPropKey()

// How far forward (degrees) both the head and tail should be from the base point
private val HeadRotationProp = FloatPropKey()
private val TailRotationProp = FloatPropKey()

// The easing for the head and tail jump
private val CircularEasing = CubicBezierEasing(0.4f, 0f, 0.2f, 1f)

// The easing for the head and tail jump
@SuppressLint("Range")
private val CircularIndeterminateTransition = transitionDefinition<Int> {
    state(0) {
        this[IterationProp] = 0
        this[BaseRotationProp] = 0f
        this[HeadRotationProp] = 0f
        this[TailRotationProp] = 0f
    }

    state(1) {
        this[IterationProp] = RotationsPerCycle
        this[BaseRotationProp] = BaseRotationAngle
        this[HeadRotationProp] = JumpRotationAngle
        this[TailRotationProp] = JumpRotationAngle
    }

    transition(fromState = 0, toState = 1) {
        IterationProp using repeatable(
            iterations = Infinite,
            animation = tween(
                durationMillis = RotationDuration * RotationsPerCycle,
                easing = LinearEasing
            )
        )
        BaseRotationProp using repeatable(
            iterations = Infinite,
            animation = tween(
                durationMillis = RotationDuration,
                easing = LinearEasing
            )
        )
        HeadRotationProp using repeatable(
            iterations = Infinite,
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at 0 with CircularEasing
                JumpRotationAngle at HeadAndTailAnimationDuration
            }
        )
        TailRotationProp using repeatable(
            iterations = Infinite,
            animation = keyframes {
                durationMillis = HeadAndTailAnimationDuration + HeadAndTailDelayDuration
                0f at HeadAndTailDelayDuration with CircularEasing
                JumpRotationAngle at durationMillis
            }
        )
    }
}

@Preview
@Composable
fun PreviewCircularProgressIndicator() {
    val list = listOf(
        0xFF0F9D58.toColor(),
        0xFFDB4437.toColor(),
        0xFF4285f4.toColor(),
        0xFFF4B400.toColor()
    )

    CircularProgressIndicator(
        colors = list,
        modifier = Modifier
            .preferredSize(36.dp)
            .padding(4.dp)
    )
}