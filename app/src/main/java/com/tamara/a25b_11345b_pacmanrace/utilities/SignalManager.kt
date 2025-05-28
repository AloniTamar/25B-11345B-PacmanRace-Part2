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

    fun vibrate(duration: Long = 500) {
        contextRef.get()?.let { context ->
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
    }

}