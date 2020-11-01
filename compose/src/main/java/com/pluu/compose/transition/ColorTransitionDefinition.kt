package com.pluu.compose.transition

import androidx.compose.animation.ColorPropKey
import androidx.compose.animation.core.AnimationConstants
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.core.tween
import androidx.compose.animation.transition
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val colorStateKey = ColorPropKey()

enum class ColorTransitionState { START, END }

fun colorTransitionDefinition(
    startColor: Color,
    endColor: Color,
    durationMillis: Int = AnimationConstants.DefaultDurationMillis
) = transitionDefinition<ColorTransitionState> {
    state(ColorTransitionState.START) {
        this[colorStateKey] = startColor
    }
    state(ColorTransitionState.END) {
        this[colorStateKey] = endColor
    }
    transition {
        colorStateKey using tween(
            durationMillis = durationMillis
        )
    }
}

@Composable
fun colorStartToEndTransition(
    definition: TransitionDefinition<ColorTransitionState>
) = transition(
    definition = definition,
    initState = ColorTransitionState.START,
    toState = ColorTransitionState.END
)
