import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.isTvDevice
import java.util.Calendar

fun AppCompatActivity.addSeasonalBackground() {
    // Get the current date
    val calendar = Calendar.getInstance()
    val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
    val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

    // Check if the device is a TV and the current date is in December (1st to 31st)
    if (isTvDevice() && currentMonth == 12 && currentDay in 1..31) {
        // Add the Christmas background if it's December
        seasonalBackground(R.drawable.christmas_header) // Replace with your Christmas image resource ID
    } else {
        removeSeasonalBackground()
        setSolidBackgroundColor()
    }
}

// Extension function to add seasonal background with a solid color below
fun AppCompatActivity.seasonalBackground(resId: Int) {
    val rootView = window.decorView.rootView as ViewGroup

    // Create a FrameLayout to hold both the solid color and seasonal background
    val container = FrameLayout(this).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    // Create a solid color background
    val solidColorView = ImageView(this).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(ContextCompat.getColor(context, R.color.fake_white)) // Replace with your desired solid color
    }

    // Create an ImageView for the seasonal background
    val seasonalImageView = ImageView(this).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scaleType = ImageView.ScaleType.FIT_XY // Stretch to fill screen
        setImageResource(resId) // Set seasonal image
    }

    // Add the solid color first and then the seasonal background
    container.addView(solidColorView)
    container.addView(seasonalImageView)

    // Add the container to the root view
    ViewCompat.setElevation(container, -1f) // Ensure it goes behind other views
    rootView.addView(container, 0) // Add it at the lowest index
}

// Remove the seasonal background by clearing the root view's children (except the solid color)
fun AppCompatActivity.removeSeasonalBackground() {
    val rootView = window.decorView.rootView as ViewGroup
    // Loop through the children and remove the seasonal background (ImageView)
    for (i in 0 until rootView.childCount) {
        val child = rootView.getChildAt(i)
        if (child is FrameLayout) {
            // Remove all the children in the FrameLayout (seasonal background)
            rootView.removeView(child)
        }
    }
}

// Function to set a solid background color
fun AppCompatActivity.setSolidBackgroundColor() {
    val rootView = window.decorView.rootView as ViewGroup

    // Create a solid color background view
    val solidColorView = ImageView(this).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setBackgroundColor(ContextCompat.getColor(context, R.color.fake_white)) // Replace with your desired solid color
    }

    // Add the solid color view to the root view
    rootView.addView(solidColorView, 0)
}
