package com.tamara.a25b_11345b_pacmanrace

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.GridLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import com.tamara.a25b_11345b_pacmanrace.utilities.SignalManager

class MainActivity : AppCompatActivity() {

    private var gameLogic: GameLogic? = null
    private var grid: Array<Array<ImageView>>? = null
    private var leftBtn: FloatingActionButton? = null
    private var rightBtn: FloatingActionButton? = null
    private var gameTimer: CountDownTimer? = null
    private var lives = 3

    companion object {
        private const val NUM_ROWS = 7
        private const val NUM_COLS = 3
        private const val MARGINS = 16
        private const val PADDING = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        SignalManager.init(this)

        gameLogic = GameLogic(
            cols = NUM_COLS,
            rows = NUM_ROWS
        )
        findViews()
        initViews()
        initListeners()
        startGame()
    }


    private fun drawPlayer() {
        val col = gameLogic?.getPlayerColumn() ?: return
        val playerCell = grid?.get(NUM_ROWS - 1)?.getOrNull(col) ?: return

        playerCell.setBackgroundResource(R.drawable.ic_pacman)
        playerCell.visibility = View.VISIBLE
    }


    private fun clearPlayer() {
        val col = gameLogic?.getPlayerColumn() ?: return
        val playerCell = grid?.get(NUM_ROWS - 1)?.getOrNull(col) ?: return

        playerCell.setBackgroundResource(R.drawable.ic_game)
        playerCell.visibility = View.INVISIBLE
    }


    private fun startGame() {
        gameTimer = object : CountDownTimer(Long.MAX_VALUE, 500) {
            override fun onTick(millisUntilFinished: Long) {
                gameLogic?.updateObstacles()
                checkPlayerCollision()
                drawObstacles()
            }

            override fun onFinish() {
                // TBD in the future
            }
        }
        gameTimer?.start()
    }

    private fun drawObstacles() {
        val matrix = gameLogic?.getObstacleMatrix() ?: return

        for (row in 0 until NUM_ROWS) {
            for (col in 0 until NUM_COLS) {
                val imageView = grid?.getOrNull(row)?.getOrNull(col) ?: continue
                imageView.visibility = if (matrix[row][col] == 1) View.VISIBLE else View.INVISIBLE
            }
        }

        drawPlayer()
    }



    private fun updateHeartsUI() {
        val hearts = listOf(
            findViewById<ImageView>(R.id.main_IMG_heart1),
            findViewById<ImageView>(R.id.main_IMG_heart2),
            findViewById<ImageView>(R.id.main_IMG_heart3)
        )

        for (i in hearts.indices) {
            hearts[i].alpha = if (i < lives) 1.0f else 0.2f
        }
    }

    private fun resetGame() {
        lives = 3
        updateHeartsUI()
        gameLogic?.resetGame()
    }

    private fun findViews() {
        leftBtn = findViewById(R.id.main_IMG_leftBtn)
        rightBtn = findViewById(R.id.main_IMG_rightBtn)

        val gridLayout = findViewById<GridLayout>(R.id.main_GRID_game)
        gridLayout.removeAllViews()
        gridLayout.rowCount = NUM_ROWS
        gridLayout.columnCount = NUM_COLS

        grid = Array(NUM_ROWS) { row ->
            Array(NUM_COLS) { col ->
                val imageView = AppCompatImageView(this)

                val params = GridLayout.LayoutParams(
                    GridLayout.spec(row, 1f),
                    GridLayout.spec(col, 1f)
                ).apply {
                    width = 0
                    height = 0
                    setMargins(MARGINS, MARGINS, MARGINS, MARGINS)
                }

                imageView.layoutParams = params
                imageView.scaleType = ImageView.ScaleType.FIT_CENTER
                imageView.setPadding(PADDING, PADDING, PADDING, PADDING)

                imageView.setBackgroundResource(R.drawable.ic_game)
                imageView.visibility = ImageView.INVISIBLE

                gridLayout.addView(imageView)
                imageView
            }
        }
    }


    private fun initViews() {
        drawPlayer()
        updateHeartsUI()
    }

    private fun initListeners() {
        leftBtn?.setOnClickListener {
            clearPlayer()
            gameLogic?.moveLeft()
            drawPlayer()
            checkPlayerCollision()
        }

        rightBtn?.setOnClickListener {
            clearPlayer()
            gameLogic?.moveRight()
            drawPlayer()
            checkPlayerCollision()
        }
    }

    private fun checkPlayerCollision() {
        if (gameLogic?.checkCollision() == true) {
            lives--
            updateHeartsUI()
            gameLogic?.resetBottomRow()
            SignalManager.getInstance().vibrate()

            if (lives == 0) {
                SignalManager.getInstance().toast("Game Over! Restarting...")

                gameLogic?.setGenerateObstacles(false)

                gameLogic?.resetGame()
                drawObstacles()

                grid?.get(0)?.get(0)?.postDelayed({
                    resetGame()
                    drawPlayer()
                    gameLogic?.setGenerateObstacles(true)
                    SignalManager.getInstance().toast("New Game Started!")
                }, 2100)
            } else {
                SignalManager.getInstance().toast("Ouch! You hit a ghost! Lives left: $lives")
            }
        }
    }

}
