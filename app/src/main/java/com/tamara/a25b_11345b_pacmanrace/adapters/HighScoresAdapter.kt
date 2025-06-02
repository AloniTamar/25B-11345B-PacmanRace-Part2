package com.tamara.a25b_11345b_pacmanrace.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tamara.a25b_11345b_pacmanrace.R
import com.tamara.a25b_11345b_pacmanrace.data.HighScore
import com.tamara.a25b_11345b_pacmanrace.fragments.MapDialogFragment
import androidx.fragment.app.FragmentActivity


class HighScoresAdapter(
    private val highScores: List<HighScore>
) : RecyclerView.Adapter<HighScoresAdapter.HighScoreViewHolder>() {

    class HighScoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val scoreText: TextView = itemView.findViewById(R.id.item_score)
        val distanceText: TextView = itemView.findViewById(R.id.item_distance)
        val dateText: TextView = itemView.findViewById(R.id.item_date)
        val locationButton: ImageButton = itemView.findViewById(R.id.item_location_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighScoreViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.high_score_item, parent, false)
        return HighScoreViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HighScoreViewHolder, position: Int) {
        val score = highScores[position]
        holder.scoreText.text = "${score.score}"
        holder.distanceText.text = "${score.distance}m"
        holder.dateText.text = score.date

        holder.locationButton.setOnClickListener {
            val context = holder.itemView.context
            if (context is FragmentActivity) {
                val fragment = MapDialogFragment.newInstance(score.latitude, score.longitude)
                fragment.show(context.supportFragmentManager, "mapDialog")
            }
        }
    }

    override fun getItemCount(): Int = highScores.size
}
