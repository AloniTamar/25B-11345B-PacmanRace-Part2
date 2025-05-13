package com.tamara.a25b_11345b_pacmanrace.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.tamara.a25b_11345b_pacmanrace.R

@SuppressLint("UseSwitchCompatOrMaterialCode")
class MainMenuActivity : AppCompatActivity() {

    private var btnStartGame: Button? = null
    private var btnViewScores: Button? = null
    private var switchSensorMovement: Switch? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        findViews()
        initListeners()
    }

    private fun findViews() {
        btnStartGame = findViewById(R.id.btn_start_game)
        btnViewScores = findViewById(R.id.btn_view_scores)
        switchSensorMovement = findViewById(R.id.switch_sensor_movement)
    }

    private fun initListeners() {
        btnStartGame?.setOnClickListener {
            val intent = Intent(this@MainMenuActivity, MainActivity::class.java)
            val sensorEnabled = switchSensorMovement?.isChecked ?: false
            intent.putExtra("EXTRA_SENSOR_ENABLED", sensorEnabled)
            startActivity(intent)
        }

        btnViewScores?.setOnClickListener {
            // TODO: Implement high scores screen logic later
        }
    }
}