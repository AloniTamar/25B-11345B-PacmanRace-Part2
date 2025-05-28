package com.tamara.a25b_11345b_pacmanrace.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

object HighScoresManager {

    private const val PREFS_NAME = "high_scores_prefs"
    private const val KEY_HIGH_SCORES = "key_high_scores"

    private lateinit var prefs: SharedPreferences
    private val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveHighScores(highScores: List<HighScore>) {
        val json = gson.toJson(highScores)
        prefs.edit { putString(KEY_HIGH_SCORES, json) }
    }

    fun loadHighScores(): MutableList<HighScore> {
        val json = prefs.getString(KEY_HIGH_SCORES, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<HighScore>>() {}.type
        return gson.fromJson(json, type)
    }

    fun addHighScore(score: HighScore) {
        val currentScores = loadHighScores()
        currentScores.add(score)

        val sorted = currentScores.sortedWith(
            compareByDescending<HighScore> { it.score }
                .thenByDescending { it.distance }
                .thenByDescending { it.date }
        )

        val top10 = sorted.take(10)

        val json = gson.toJson(top10)
        prefs.edit().putString(KEY_HIGH_SCORES, json).apply()
    }

    fun clearAll() {
        prefs.edit { remove(KEY_HIGH_SCORES) }
    }
}
