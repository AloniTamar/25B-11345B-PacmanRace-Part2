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

