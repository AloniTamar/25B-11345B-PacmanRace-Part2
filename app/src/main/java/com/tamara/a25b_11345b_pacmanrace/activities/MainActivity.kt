package com.tamara.a25b_11345b_pacmanrace.activities

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.GridLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import com.tamara.a25b_11345b_pacmanrace.logic.GameLogic
import com.tamara.a25b_11345b_pacmanrace.R
import com.tamara.a25b_11345b_pacmanrace.utilities.SignalManager
import com.tamara.a25b_11345b_pacmanrace.data.HighScoresManager
import com.tamara.a25b_11345b_pacmanrace.data.HighScore
import com.tamara.a25b_11345b_pacmanrace.utilities.SingleSoundPlayer
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private var gameLogic: GameLogic? = null
    private var grid: Array<Array<ImageView>>? = null
    private var leftBtn: FloatingActionButton? = null
    private var rightBtn: FloatingActionButton? = null
    private var gameTimer: CountDownTimer? = null
    private var scoreTextView: TextView? = null
    private var soundPlayer: SingleSoundPlayer? = null
    private var useSensor: Boolean = false
    private var playerLat: Double = 0.0
    private var playerLon: Double = 0.0
    private var distanceTextView: TextView? = null


    companion object {
        private const val NUM_ROWS = 11
        private const val NUM_COLS = 5
        private const val MARGINS = 16
        private const val PADDING = 8
        private var lives = 3
        private var score = 0
        private var distance = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        HighScoresManager.init(this)

        setContentView(R.layout.activity_main)
        soundPlayer = SingleSoundPlayer(this)
        useSensor = intent.getBooleanExtra("EXTRA_SENSOR_ENABLED", false)
        playerLat = intent.getDoubleExtra("EXTRA_LATITUDE", 0.0)
        playerLon = intent.getDoubleExtra("EXTRA_LONGITUDE", 0.0)

        SignalManager.init(this)

        gameLogic = GameLogic(
            cols = NUM_COLS,
            rows = NUM_ROWS
        )
        findViews()
        initViews()
        initListeners()

        resetGame()
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

        playerCell.setBackgroundResource(R.drawable.ic_ghost_green)
        playerCell.visibility = View.INVISIBLE
    }


    private fun startGame() {
        gameTimer = object : CountDownTimer(Long.MAX_VALUE, 500) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val moved = gameLogic?.updateObstacles() ?: false
                if (moved) {
                    distance += 100
                    distanceTextView?.text = "Distance: $distance"
                }

                checkPlayerCollision()
                drawObstacles()
                checkCoinCollection()
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
                when (matrix[row][col]) {
                    1 -> {
                        imageView.setBackgroundResource(R.drawable.ic_ghost_blue)
                        imageView.visibility = View.VISIBLE
                    }
                    2 -> {
                        imageView.setBackgroundResource(R.drawable.ic_ghost_green)
                        imageView.visibility = View.VISIBLE
                    }
                    3 -> {
                        imageView.setBackgroundResource(R.drawable.ic_ghost_red)
                        imageView.visibility = View.VISIBLE
                    }
                    4 -> {
                        imageView.setBackgroundResource(R.drawable.ic_coin)
                        imageView.visibility = View.VISIBLE
                    }
                    else -> {
                        imageView.visibility = View.INVISIBLE
                    }
                }
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

    @SuppressLint("SetTextI18n")
    private fun resetGame() {
        lives = 3
        score = 0
        distance = 0
        updateHeartsUI()
        gameLogic?.resetGame()
        scoreTextView?.text = "Score: $score"
    }


    private fun findViews() {
        leftBtn = findViewById(R.id.main_IMG_leftBtn)
        rightBtn = findViewById(R.id.main_IMG_rightBtn)

        scoreTextView = findViewById(R.id.scoreTextView)
        distanceTextView = findViewById(R.id.distanceTextView)

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

                imageView.setBackgroundResource(R.drawable.ic_ghost_green)
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
            checkCoinCollection()
        }

        rightBtn?.setOnClickListener {
            clearPlayer()
            gameLogic?.moveRight()
            drawPlayer()
            checkPlayerCollision()
            checkCoinCollection()
        }
    }

    private fun checkPlayerCollision() {
        if (gameLogic?.checkCollision() == true) {
            lives--
            updateHeartsUI()
            gameLogic?.resetBottomRow()
            SignalManager.getInstance().vibrate()

            if (lives == 0) {
                SignalManager.getInstance().toast("Game Over! Score: $score\"")
                soundPlayer?.playSound(R.raw.end_game_sound)
                val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val highScore = HighScore(
                    score = score,
                    distance = distance,
                    date = currentDate,
                    mode = useSensor,
                    latitude = playerLat,
                    longitude = playerLon
                )
                HighScoresManager.addHighScore(highScore)


                gameLogic?.setGenerateObstacles(false)

                gameLogic?.resetGame()
                drawObstacles()

                grid?.get(0)?.get(0)?.postDelayed({
                    finish()
                }, 2100)
            } else {
                soundPlayer?.playSound(R.raw.crash_sound)
                SignalManager.getInstance().toast("Ouch! You hit a ghost! Lives left: $lives")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkCoinCollection() {
        val col = gameLogic?.getPlayerColumn() ?: return
        val matrix = gameLogic?.getObstacleMatrix() ?: return

        if (matrix[NUM_ROWS - 1][col] == 4) {
            soundPlayer?.playSound(R.raw.coin_sound)
            score += 1
            scoreTextView?.text = "Score: $score"
            gameLogic?.resetBottomRow()
        }
    }

//    @SuppressLint("SetTextI18n")
//    private fun updateDistanceUI() {
//        val scoreText = "Score: $score"
//        val distanceText = "Distance: $distance"
//        scoreTextView?.text = "$scoreText\n$distanceText"
//    }

    override fun onPause() {
        super.onPause()
        gameTimer?.cancel()
    }
}
