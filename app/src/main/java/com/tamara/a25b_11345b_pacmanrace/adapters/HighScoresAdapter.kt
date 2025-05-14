package com.tamara.a25b_11345b_pacmanrace.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tamara.a25b_11345b_pacmanrace.R
import com.tamara.a25b_11345b_pacmanrace.data.HighScore

class HighScoresAdapter(
    private val highScores: List<HighScore>
) : RecyclerView.Adapter<HighScoresAdapter.HighScoreViewHolder>() {

    class HighScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoreText: TextView = itemView.findViewById(R.id.item_score)
        val distanceText: TextView = itemView.findViewById(R.id.item_distance)
        val dateText: TextView = itemView.findViewById(R.id.item_date)
        val locationButton: ImageButton = itemView.findViewById(R.id.item_location_btn)
        val modeIcon: ImageButton = itemView.findViewById(R.id.item_mode_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.high_score_item, parent, false)
        return HighScoreViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val score = highScores[position]
        holder.scoreText.text = "Score: ${score.score}"
        holder.distanceText.text = "Distance: ${score.distance}m"
        holder.dateText.text = score.date

        // Placeholder - to be implemented later
        holder.locationButton.setOnClickListener {
            // Show location popup in the future
        }

        val iconRes = if (score.mode) {
            R.drawable.ic_mode_fast // rabbit
        } else {
            R.drawable.ic_mode_slow // turtle
        }
        holder.modeIcon.setImageResource(iconRes)

    }

    override fun getItemCount(): Int = highScores.size
}
