package com.kitaotao.sst

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import java.io.File

open class BaseActivity : AppCompatActivity() {

    // Define the idle timeout duration (e.g., 5 minutes)
    private val idleTimeout: Long = 600000 // 600,000 ms = 10 minutes
    private var idleHandler: Handler? = null
    private val idleRunnable = Runnable {
        // Navigate to the Post Screen when idle
        val intent = Intent(this, postScreen::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        idleHandler = Handler(Looper.getMainLooper())
        resetIdleTimer()

        initializeOSMDroid()

        Configuration.getInstance().userAgentValue = packageName
    }

    // Flag to track the state of the overlay image (whether it's enlarged or not)
    private var isEnlarged = false

    /**
     * This function should be called from the specific Activity (like TOURISM.kt)
     * to handle the overlay image functionality.
     */
    fun setupOverlayImage(mapView: MapView, overlayImage: ImageView) {
        overlayImage.setOnClickListener {
            if (isEnlarged) {
                // If the image is enlarged, shrink it back
                shrinkImage(overlayImage)
            } else {
                // If the image is not enlarged, enlarge it with breathing effect
                enlargeImage(mapView, overlayImage)
            }
        }
    }

    /**
     * Enlarge the overlay image with a breathing effect.
     */
    private fun enlargeImage(mapView: MapView, overlayImage: ImageView) {
        val originalWidth = overlayImage.width
        val originalHeight = overlayImage.height

        val enlargedWidth = mapView.width
        val enlargedHeight = mapView.height

        // Animate the image resize with breathing effect
        animateImageResizeWithBreathing(overlayImage, originalWidth, originalHeight, enlargedWidth, enlargedHeight)

        // Set the flag to indicate the image is enlarged
        isEnlarged = true
    }

    /**
     * Shrink the overlay image back to its original size.
     */
    private fun shrinkImage(overlayImage: ImageView) {
        // Convert 100dp to pixels
        val context = overlayImage.context
        val widthInPixels = (100 * context.resources.displayMetrics.density).toInt()  // Convert dp to pixels

        // Get the original width and height of the image
        val originalWidth = overlayImage.layoutParams.width

        // Create a ValueAnimator to animate the size change
        val animator = ValueAnimator.ofInt(originalWidth, widthInPixels)
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            overlayImage.layoutParams.width = animatedValue
            overlayImage.layoutParams.height = animatedValue
            overlayImage.requestLayout()  // Request layout to apply changes
        }
        animator.duration = 300  // Set animation duration (300ms)
        animator.start()

        // Set the flag to indicate the image is not enlarged
        isEnlarged = false
    }

    /**
     * Animates the resizing of the overlay image, with a breathing effect.
     * It briefly expands the image slightly beyond the target size, then shrinks back to the correct size.
     */
    private fun animateImageResizeWithBreathing(
        overlayImage: ImageView,
        startWidth: Int,
        startHeight: Int,
        endWidth: Int,
        endHeight: Int
    ) {
        val breathingWidth = (endWidth * 1.1).toInt()  // Slightly increase width (10% larger)
        val breathingHeight = (endHeight * 1.1).toInt()  // Slightly increase height (10% larger)

        // First animation: Expand to slightly larger size
        val firstAnimator = ValueAnimator.ofFloat(0f, 1f)
        firstAnimator.addUpdateListener { animation ->
            val progress = animation.animatedFraction
            val newWidth = startWidth + (breathingWidth - startWidth) * progress
            val newHeight = startHeight + (breathingHeight - startHeight) * progress

            overlayImage.layoutParams.width = newWidth.toInt()
            overlayImage.layoutParams.height = newHeight.toInt()
            overlayImage.requestLayout()
        }
        firstAnimator.duration = 150 // Shorter duration for initial "breathing" phase
        firstAnimator.start()

        // Second animation: Shrink to the correct target size
        firstAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                val secondAnimator = ValueAnimator.ofFloat(0f, 1f)
                secondAnimator.addUpdateListener { animation ->
                    val progress = animation.animatedFraction
                    val newWidth = breathingWidth + (endWidth - breathingWidth) * progress
                    val newHeight = breathingHeight + (endHeight - breathingHeight) * progress

                    overlayImage.layoutParams.width = newWidth.toInt()
                    overlayImage.layoutParams.height = newHeight.toInt()
                    overlayImage.requestLayout()
                }
                secondAnimator.duration = 150 // Duration for the shrinking back to the final size
                secondAnimator.start()
            }
        })
    }

    /**
     * Helper function to animate the image size smoothly.
     */
    private fun animateImageResize(
        overlayImage: ImageView,
        startWidth: Int,
        startHeight: Int,
        endWidth: Int,
        endHeight: Int
    ) {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            val progress = animation.animatedFraction
            val newWidth = startWidth + (endWidth - startWidth) * progress
            val newHeight = startHeight + (endHeight - startHeight) * progress

            overlayImage.layoutParams.width = newWidth.toInt()
            overlayImage.layoutParams.height = newHeight.toInt()
            overlayImage.requestLayout()
        }
        animator.duration = 300
        animator.start()
    }


    fun initializeMap(mapView: MapView) {
        mapView.setTileSource(object : OnlineTileSourceBase(
            "GoogleMaps_Satellite",
            0, 20, 256, "",
            arrayOf("https://mt1.google.com/vt/lyrs=s&x={x}&y={y}&z={z}")
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val zoom = MapTileIndex.getZoom(pMapTileIndex)
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                return "https://mt1.google.com/vt/lyrs=s&x=$x&y=$y&z=$zoom"
            }
        })
    }

    private fun initializeOSMDroid() {
        // OSMDroid configuration
        val configuration = Configuration.getInstance()

        // Set user agent dynamically based on your app
        configuration.userAgentValue = packageName

        // Set up the cache directory for tiles
        val cacheDir = File(getExternalFilesDir(null), "osmdroid/tiles")
        configuration.osmdroidTileCache = cacheDir

        // Configure tile cache size
        configuration.tileFileSystemCacheTrimBytes = 200L * 1024 * 1024 // 200 MB
        configuration.tileFileSystemCacheMaxBytes = 500L * 1024 * 1024 // 500 MB

        // Set expiration time for cached tiles (e.g., 7 days)
        configuration.expirationOverrideDuration = 1L * 24 * 60 * 60 * 1000 // 1 day
    }

    // Reset the timer whenever there's user interaction
    override fun onUserInteraction() {
        super.onUserInteraction()
        resetIdleTimer()
    }

    private fun resetIdleTimer() {
        idleHandler?.removeCallbacks(idleRunnable)
        idleHandler?.postDelayed(idleRunnable, idleTimeout)
    }

    override fun onDestroy() {
        super.onDestroy()
        idleHandler?.removeCallbacks(idleRunnable)
    }
}
