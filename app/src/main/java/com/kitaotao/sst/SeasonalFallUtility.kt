package com.kitaotao.sst

import HalloweenFallView
import HeartFallView
import SnowfallView
import android.content.Context
import android.view.ViewGroup
import java.util.Calendar

object SeasonalFallUtility {

    // Function to add either SnowfallView or HeartFallView based on the season
    fun ViewGroup.addSeasonalFall(context: Context) {
        // Get the current date
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Decide which view to show (SnowfallView or HeartFallView)
        val seasonalView = if (currentMonth == 12 && currentDay in 1..31) {
            SnowfallView(context)
        } else if (currentMonth == 2 && currentDay in 1..14) {
            HeartFallView(context)
        } else if (currentMonth == 11 && currentDay in 1..31){
            HalloweenFallView(context)
        } else {
            null
        }

        seasonalView?.let {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            // Add the chosen view as the first child, ensuring it's in the background
            this.addView(it, 0)
        }
    }
}