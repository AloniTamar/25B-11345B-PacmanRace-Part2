package com.tamara.a25b_11345b_pacmanrace.utilities

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import com.tamara.a25b_11345b_pacmanrace.interfaces.TiltCallback
import kotlin.math.abs

class TiltDetector(context: Context, private var tiltCallback: TiltCallback?) {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    private var sensorEventListener: SensorEventListener? = null
    private var lastTimestamp = 0L

    fun start() {
        if (sensorEventListener == null) {
            sensorEventListener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent) {
                    val x = -event.values[0]
                    val y = event.values[1]
                    val now = System.currentTimeMillis()
                    if (now - lastTimestamp >= 300) {
                        lastTimestamp = now
                        if (abs(x) > 3.0f) {
                            tiltCallback?.tiltX(x)
                        }
                        if (abs(y) > 1.0f) {
                            tiltCallback?.tiltY(y)
                        }

                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            }
        }

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_GAME
        )
    }

    fun stop() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }
}
