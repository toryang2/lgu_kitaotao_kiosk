import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable

/**
 * Extension function for LottieAnimationView to play the animation from the start
 * but loop from a specific time onward.
 *
 * @param loopStartTimeMs The time (in milliseconds) where the animation should loop.
 */
fun LottieAnimationView.playWithLoopFrom(loopStartTimeMs: Long) {
    // Start the animation immediately
    this.playAnimation()

    this.addAnimatorListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            // Get the total duration of the animation
            val totalDuration = this@playWithLoopFrom.duration // Total duration in ms

            // Calculate the start position for looping in terms of progress
            val loopStartProgress = loopStartTimeMs.toFloat() / totalDuration
            val startFrame = (loopStartProgress * this@playWithLoopFrom.maxFrame).toInt()

            // Set the start and end frames for looping
            this@playWithLoopFrom.setMinAndMaxFrame(startFrame, this@playWithLoopFrom.maxFrame.toInt())

            // Repeat the animation infinitely after the specified time
            this@playWithLoopFrom.repeatCount = LottieDrawable.INFINITE

            // To ensure we don't reset the animation, call playAnimation without resetting the start time
            this@playWithLoopFrom.playAnimation()
        }
    })
}
