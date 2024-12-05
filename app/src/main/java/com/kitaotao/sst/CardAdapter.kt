package com.kitaotao.sst

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

// Data class for grid items
data class GridItem(val title: String, val image: Drawable, val targetActivity: Class<*>)

class CardAdapter(private val items: List<GridItem>, private val context: Context) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    // ViewHolder for the grid items
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: CardView = view.findViewById(R.id.cardView)
        val imageView: ImageView = view.findViewById(R.id.cardImage)
        val textView: TextView = view.findViewById(R.id.cardText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.grid_card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item.title
        Glide.with(holder.imageView.context)
            .load(item.image)
            .into(holder.imageView)

        // Dynamically adjust the top margin for the first three items
        val layoutParams = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
        if (position < 10) {
            layoutParams.topMargin = 600 // Apply 600dp margin directly (in px)
        } else {
            layoutParams.topMargin = 16 // Default margin for other items (in px)
        }
        holder.cardView.layoutParams = layoutParams

        // Set the click listener to navigate to the target activity
        holder.cardView.setOnClickListener {
            val intent = Intent(context, item.targetActivity)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
