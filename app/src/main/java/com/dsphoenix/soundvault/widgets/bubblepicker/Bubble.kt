package com.dsphoenix.soundvault.widgets.bubblepicker

import kotlin.math.sqrt

data class Bubble(val centerX: Float = 0f, val centerY: Float = 0f, val radius: Float = 0f)

fun Bubble.isInArea(x: Float, y: Float): Boolean {
    val rangeX = x - centerX
    val rangeY = y - centerY
    return sqrt(rangeX * rangeX + rangeY * rangeY) <= radius
}
