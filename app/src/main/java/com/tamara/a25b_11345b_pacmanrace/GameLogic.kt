package com.tamara.a25b_11345b_pacmanrace

import kotlin.random.Random

class GameLogic(private val rows: Int = 7, private val cols: Int = 3) {

    private val obstacleMatrix: Array<IntArray> = Array(rows) { IntArray(cols) { 0 } }
    private var generateObstacle = true
    private var generateObstacles = true

    companion object {
        private var PLAYER_COL = 1
    }

    fun getPlayerColumn(): Int = PLAYER_COL

    fun getObstacleMatrix(): Array<IntArray> = obstacleMatrix.map { it.clone() }.toTypedArray()

    fun moveLeft() {
        if (PLAYER_COL > 0) PLAYER_COL--
    }

    fun moveRight() {
        if (PLAYER_COL < cols - 1) PLAYER_COL++
    }

    fun updateObstacles() {
        if (!generateObstacles) return
        for (row in rows - 2 downTo 0) {
            for (col in 0 until cols) {
                obstacleMatrix[row + 1][col] = obstacleMatrix[row][col]
            }
        }

        for (col in 0 until cols) {
            obstacleMatrix[0][col] = 0
        }

        if (generateObstacle) {
            obstacleMatrix[0][Random.nextInt(cols)] = 1
        }

        generateObstacle = !generateObstacle
    }

    fun checkCollision(): Boolean {
        return obstacleMatrix[rows - 1][PLAYER_COL] == 1
    }

    fun resetBottomRow() {
        for (col in 0 until cols) {
            obstacleMatrix[rows - 1][col] = 0
        }
    }

    fun resetGame() {
        PLAYER_COL = 1
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                obstacleMatrix[row][col] = 0
            }
        }
    }
    fun setGenerateObstacles(enabled: Boolean) {
        generateObstacles = enabled
    }
}
