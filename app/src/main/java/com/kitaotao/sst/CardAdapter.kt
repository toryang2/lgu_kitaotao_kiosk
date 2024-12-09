package com.kitaotao.sst

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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

        // Load the image using Glide
        Glide.with(holder.imageView.context)
            .load(item.image)
            .into(holder.imageView)

        // Dynamically adjust the top margin for TV or phone
        val layoutParams = holder.cardView.layoutParams as ViewGroup.MarginLayoutParams
        val isTv = isTvDevice(context)
        if (isTv) {
            layoutParams.topMargin = if (position < 10) 20 else 16 // Adjusted margins for TV
        } else {
            layoutParams.topMargin = if (position < 3) 20 else 16 // Adjusted margins for phone
            layoutParams.leftMargin = 16
            layoutParams.rightMargin = 16
            layoutParams.width = 320
            layoutParams.height = 320
        }

        holder.cardView.layoutParams = layoutParams

        if (!isTv) {
            holder.textView.setPadding(dpToPx(8,context), dpToPx(8,context), dpToPx(8,context), dpToPx(8,context))
        }

        // Set the click listener to navigate to the target activity
        holder.cardView.setOnClickListener {
            val intent = Intent(context, item.targetActivity)
            context.startActivity(intent)
        }
    }

    private fun dpToPx(dp: Int, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Helper method to check if the device is a TV
    private fun isTvDevice(context: Context): Boolean {
        return context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }
}
