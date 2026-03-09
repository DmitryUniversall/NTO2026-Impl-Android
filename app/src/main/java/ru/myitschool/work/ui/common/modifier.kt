package ru.myitschool.work.ui.common

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp

@Composable
fun Modifier.objectClickable(onClick: () -> Unit) =
    this.clickable(
        indication = ripple(),
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )

@Composable
fun Modifier.objectClickableNoAnimation(onClick: () -> Unit) =
    this.clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = onClick
    )

fun Modifier.withShapeBackground(
    color: Color,
    shape: Shape
) =
    this
        .background(
            color = color,
            shape = shape
        )
        .clip(shape = shape)

fun Modifier.withShapeBackground(
    gradient: Brush,
    shape: Shape
) =
    this
        .background(
            brush = gradient,
            shape = shape
        )
        .clip(shape = shape)

fun Modifier.shimmer(
    shape: Shape,
    baseColor: Color,
    highlightColor: Color
): Modifier = composed {
    val transition = rememberInfiniteTransition()
    val x = transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing)
        )
    ).value

    val brush = Brush.linearGradient(
        colors = listOf(baseColor, highlightColor, baseColor),
        start = Offset(x, 0f),
        end = Offset(x + 400f, 0f)
    )

    this
        .clip(shape)
        .background(brush)
}


fun Modifier.borderBottom(
    width: Dp,
    color: Color
) = drawBehind {
    drawLine(
        color = color,
        start = Offset(0f, size.height),
        end = Offset(size.width, size.height),
        strokeWidth = width.toPx()
    )
}
