import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
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
    }
}

// Extension function to add a Christmas background
fun AppCompatActivity.seasonalBackground(resId: Int) {
    val rootView = window.decorView.rootView as ViewGroup

    // Create an ImageView to act as an overlay
    val imageView = ImageView(this).apply {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        scaleType = ImageView.ScaleType.FIT_XY // Stretch to fill screen
        setImageResource(resId) // Set Christmas image
        alpha = 1f // Slight transparency to show the original background
    }

    // Add the overlay to the root view
    ViewCompat.setElevation(imageView, -1f) // Ensure it goes behind other views
    rootView.addView(imageView, 0) // Add it at the lowest index
}