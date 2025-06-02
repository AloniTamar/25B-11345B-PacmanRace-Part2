package com.tamara.a25b_11345b_pacmanrace.interfaces

interface TiltCallback {
    fun tiltX(direction: Float)
    fun tiltY(y: Float)
}

fun TiltCallback.handleTiltX(direction: Float, moveLeft: () -> Unit, moveRight: () -> Unit) {
    if (direction > 0) {
        moveRight()
    } else {
        moveLeft()
    }
}

fun TiltCallback.handleTiltY(
    y: Float,
    currentSpeedIndex: Int,
    onSpeedChange: (newSpeedIndex: Int) -> Unit
) {
    val slowThreshold = 2.5f
    val fastThreshold = 7.5f

    when {
        y >= fastThreshold && currentSpeedIndex != 1 -> {
            onSpeedChange(1)
        }
        y <= slowThreshold && currentSpeedIndex != 0 -> {
            onSpeedChange(0)
        }
    }
}

