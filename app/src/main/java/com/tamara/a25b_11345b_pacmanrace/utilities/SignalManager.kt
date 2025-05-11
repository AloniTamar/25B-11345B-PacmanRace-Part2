package com.tamara.a25b_11345b_pacmanrace.utilities

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import java.lang.ref.WeakReference

class SignalManager private constructor(context: Context) {
    private val contextRef = WeakReference(context)

    companion object {
        @Volatile
        private var instance: SignalManager? = null

        fun init(context: Context): SignalManager {
            return instance ?: synchronized(this) {
                instance ?: SignalManager(context).also { instance = it }
            }
        }

        fun getInstance(): SignalManager {
            return instance ?: throw IllegalStateException(
                "SignalManager must be initialized by calling init(context) before use."
            )
        }
    }

    fun toast(text: String) {
        contextRef.get()?.let { context ->
            android.widget.Toast
                .makeText(
                    context,
                    text,
                    android.widget.Toast.LENGTH_SHORT
                )
                .show()
        }
    }

    fun vibrate() {
        contextRef.get()?.let { context: Context ->
            val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator
            } else {
                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val SOSPattern = longArrayOf(
                    0, 200, 100, 200, 100, 200,
                    300, 500, 100, 500, 100, 500,
                    300, 200, 100, 200, 100, 200
                )

                val waveFormVibrationEffect = VibrationEffect.createWaveform(
                    SOSPattern,
                    -1
                )

                vibrator.vibrate(waveFormVibrationEffect)
            } else {
                vibrator.vibrate(500L)
            }
        }
    }

}