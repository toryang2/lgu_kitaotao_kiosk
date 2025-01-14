// Extension.kt
import android.content.res.Configuration
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.kitaotao.sst.R

// Hide LinearLayout for non-TV devices and adjust the ScrollView layout
fun AppCompatActivity.officeViewChange() {
    val linearLayout = findViewById<LinearLayout>(R.id.nonScrollableRight)
    val scrollLayout = findViewById<ScrollView>(R.id.scrollableLeft)

    if (!isTvDevice()) {

        // Adjust ScrollView dimensions if it is inside a ConstraintLayout
        if (scrollLayout.layoutParams is ConstraintLayout.LayoutParams) {
            val constraintParams = scrollLayout.layoutParams as ConstraintLayout.LayoutParams

            // Set width to match_parent
            constraintParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            // Set height to 50% of the parent's height
            constraintParams.height = 0

            constraintParams.matchConstraintPercentHeight = 0.7f

            // Remove the bottom constraint
            constraintParams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET

            // Apply the updated layout parameters
            scrollLayout.layoutParams = constraintParams

            val linearParams = linearLayout.layoutParams as ConstraintLayout.LayoutParams

            linearParams.width = ConstraintLayout.LayoutParams.MATCH_PARENT

            linearParams.height = 0

            linearParams.matchConstraintPercentHeight = 0.3f

            linearParams.topToBottom = R.id.scrollableLeft

            linearParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID

            linearParams.topToTop = ConstraintLayout.LayoutParams.UNSET

            linearLayout.layoutParams = linearParams
        }
    }
}
// Check if the device is a TV
private fun AppCompatActivity.isTvDevice(): Boolean {
    return resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
}

// Check if the device is a TV
fun AppCompatActivity.isDeviceTabletClickPop(): Boolean {
    val screenLayout = resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK
    return screenLayout == Configuration.SCREENLAYOUT_SIZE_LARGE ||
            screenLayout == Configuration.SCREENLAYOUT_SIZE_XLARGE
}