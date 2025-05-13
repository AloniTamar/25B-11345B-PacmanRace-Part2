package com.tamara.a25b_11345b_pacmanrace.logic

import kotlin.random.Random

class GameLogic(private val rows: Int = 11, private val cols: Int = 5) {

    private val obstacleMatrix: Array<IntArray> = Array(rows) { IntArray(cols) { 0 } }
    private var generateObstacles = true
    private var emptyRowCooldown = 0

    companion object {
        private var PLAYER_COL = 2
    }

    fun getPlayerColumn(): Int = PLAYER_COL

    fun getObstacleMatrix(): Array<IntArray> = obstacleMatrix.map { it.clone() }.toTypedArray()

    fun moveLeft() {
        if (PLAYER_COL > 0) PLAYER_COL--
    }

    fun moveRight() {
        if (PLAYER_COL < cols - 1) PLAYER_COL++
    }

    fun updateObstacles(): Boolean {
        if (!generateObstacles) return false

        for (row in rows - 2 downTo 0) {
            for (col in 0 until cols) {
                obstacleMatrix[row + 1][col] = obstacleMatrix[row][col]
            }
        }

        for (col in 0 until cols) {
            obstacleMatrix[0][col] = 0
        }

        if (emptyRowCooldown > 0) {
            emptyRowCooldown--
            return true
        }

        val chance = Random.Default.nextInt(100)

        if (chance in 0..49) {
            val ghostCol = Random.Default.nextInt(cols)
            val ghostType = Random.Default.nextInt(1, 4)
            obstacleMatrix[0][ghostCol] = ghostType

        } else if (chance in 50..66) {
            val coinCol = Random.Default.nextInt(cols)
            obstacleMatrix[0][coinCol] = 4

        } else if (chance in 67..84) {
            val ghostCol = Random.Default.nextInt(cols)
            val ghostType = Random.Default.nextInt(1, 4)
            obstacleMatrix[0][ghostCol] = ghostType

            var coinCol = Random.Default.nextInt(cols)
            while (coinCol == ghostCol) {
                coinCol = Random.Default.nextInt(cols)
            }
            obstacleMatrix[0][coinCol] = 4
        }

        emptyRowCooldown = 1
        return true
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
        PLAYER_COL = 2
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