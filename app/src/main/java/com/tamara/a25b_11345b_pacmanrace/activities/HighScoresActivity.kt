package com.tamara.a25b_11345b_pacmanrace.activities

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.tamara.a25b_11345b_pacmanrace.R
import com.tamara.a25b_11345b_pacmanrace.adapters.HighScoresAdapter
import com.tamara.a25b_11345b_pacmanrace.data.HighScore
import com.tamara.a25b_11345b_pacmanrace.data.HighScoresManager


class HighScoresActivity : AppCompatActivity() {

    private var sortingSpinner: Spinner? = null
    private var filterSpinner: Spinner? = null
    private var recyclerView: RecyclerView? = null
    private var adapter: HighScoresAdapter? = null
    private var allScores: List<HighScore> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)
        HighScoresManager.init(this)
        sortingSpinner = findViewById(R.id.high_scores_spinner)
        filterSpinner = findViewById(R.id.high_scores_filter_spinner)
        recyclerView = findViewById(R.id.high_scores_recycler)

        val backBtn: MaterialButton = findViewById(R.id.high_scores_back_btn)
        backBtn.setOnClickListener {
            finish() // Go back to MainMenuActivity
        }

        recyclerView?.layoutManager = LinearLayoutManager(this)

        allScores = HighScoresManager.loadHighScores()
        updateList()

        sortingSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        filterSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateList()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun updateList() {
        var filtered = when (filterSpinner?.selectedItemPosition) {
            else -> allScores
        }

        filtered = when (sortingSpinner?.selectedItemPosition) {
            1 -> filtered.sortedByDescending { it.distance }
            else -> filtered.sortedByDescending { it.score }
        }

        adapter = HighScoresAdapter(filtered)
        recyclerView?.adapter = adapter
    }
}
