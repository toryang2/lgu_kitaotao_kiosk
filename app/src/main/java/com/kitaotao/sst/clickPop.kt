import android.app.Activity
import android.icu.util.Calendar
import android.view.MotionEvent
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.kitaotao.sst.R

fun Activity.showClickPopAnimation(event: MotionEvent) {
    if (event.action == MotionEvent.ACTION_DOWN) {
        // Access the root view of the activity using window.decorView
        val rootView = window.decorView.findViewById<FrameLayout>(android.R.id.content)
        // Create and configure the Lottie animation view

        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        val size = 400
        val offset = size / 2

        val lottieView = LottieAnimationView(this).apply {
            when {
                currentMonth == 2 && currentDay in 1..14 -> {
                    setAnimation(R.raw.clickpop_valentines)
                    layoutParams = FrameLayout.LayoutParams(size, size).apply {
                        leftMargin = (event.rawX - offset).toInt()
                        topMargin = (event.rawY - offset).toInt()
                    }
                } currentMonth == 12 && currentDay in 1..31 -> {
                    setAnimation(R.raw.clickpop_christmas)
                    layoutParams = FrameLayout.LayoutParams(size, size).apply {
                        leftMargin = (event.rawX - offset).toInt()
                        topMargin = (event.rawY - offset).toInt()
                    }
                }
                else -> {
                    setAnimation(R.raw.clickpop_default) // Set the Lottie animation
                    layoutParams = FrameLayout.LayoutParams(size, size).apply {
                        leftMargin = (event.rawX - offset).toInt()
                        topMargin = (event.rawY - offset).toInt()
                    }
                    repeatCount = 0 // Ensure it only plays once
                }
            }
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