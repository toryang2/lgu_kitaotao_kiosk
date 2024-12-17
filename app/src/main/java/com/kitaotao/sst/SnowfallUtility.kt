package com.kitaotao.sst

import SnowfallView
import android.content.Context
import android.view.ViewGroup

object SnowFallUtility {

    // Function to add SnowfallView programmatically to any ViewGroup (e.g., Activity layout)
    fun ViewGroup.addSnowfall(context: Context) {
        val snowfallView = SnowfallView(context)
        snowfallView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        // Add SnowfallView as the first child, ensuring it's in the background
        this.addView(snowfallView, 0)
    }
}