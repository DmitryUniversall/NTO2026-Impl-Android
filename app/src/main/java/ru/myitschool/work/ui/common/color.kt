package ru.myitschool.work.ui.common

import androidx.compose.ui.graphics.Color

fun Color.muted(
    alpha: Float = 0.5f
): Color = this.copy(alpha = alpha)
