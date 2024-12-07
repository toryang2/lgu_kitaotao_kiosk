import android.view.MotionEvent
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.kitaotao.sst.R

fun Activity.showClickPopAnimation(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
        // Access the root view of the activity using window.decorView
        val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
        // Create and configure the Lottie animation view

        val size = 300

        val lottieView = LottieAnimationView(this).apply {
            setAnimation(R.raw.click_pop_animation) // Set the Lottie animation
            layoutParams = FrameLayout.LayoutParams(size, size).apply {
                leftMargin = (event.rawX - size / 2).toInt()
                topMargin = (event.rawY - size / 2).toInt()
            }
            repeatCount = 0 // Ensure it only plays once
        }

        // Add the Lottie view to the root layout and start the animation
        rootView.addView(lottieView)
        lottieView.playAnimation()

        // Remove the Lottie animation after it finishes
        lottieView.postDelayed({
            rootView.removeView(lottieView)
        }, 500L)
    }
}