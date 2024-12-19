package com.kitaotao.sst

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.compass.CompassOverlay
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

    // Static API key (static value)
    companion object {
        const val API_KEY = "5816775e-afb6-4912-b64e-be7a1066183a"  // Replace with your actual API key
    }

    // Method to create the GraphHopper URL
    fun createGraphHopperUrl(firstMarkerPoint: GeoPoint, secondMarkerPoint: GeoPoint): String {
        return "https://graphhopper.com/api/1/route?point=${firstMarkerPoint.latitude},${firstMarkerPoint.longitude}&point=${secondMarkerPoint.latitude},${secondMarkerPoint.longitude}&vehicle=foot&key=$API_KEY"
    }


    // Function to decode polyline
    fun decodePolyline(encoded: String): List<GeoPoint> {
        val poly = ArrayList<GeoPoint>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].code - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val geoPoint = GeoPoint(lat / 1E5, lng / 1E5)
            poly.add(geoPoint)
        }
        return poly
    }


    // Flag to track the state of the overlay image (whether it's enlarged or not)
    private var isEnlarged = false

    /**
     * This function should be called from the specific Activity (like TOURISM.kt)
     * to handle the overlay image functionality.
     */
    fun setupOverlayImage(view: View, overlayImage: ImageView) {
        overlayImage.setOnClickListener {
            if (isEnlarged) {
                // If the image is enlarged, shrink it back
                shrinkImage(overlayImage)
            } else {
                // If the image is not enlarged, enlarge it with breathing effect
                enlargeImage(view, overlayImage)
            }
        }
    }

    fun startBreathingAnimation(linearLayout: LinearLayout) {
        // Define the start and end colors for the background
        val startBackgroundColor = Color.parseColor("#FFFFFF")  // White color
        val endBackgroundColor = Color.parseColor("#4CAF50")    // Green color

        // Stroke color remains constant
        val strokeColor = Color.parseColor("#000000")  // Black stroke color

        // Create the color animator for the background color
        val backgroundColorAnimator = ValueAnimator.ofObject(ArgbEvaluator(), startBackgroundColor, endBackgroundColor)
        backgroundColorAnimator.duration = 1000 // Duration for one cycle (1 second)
        backgroundColorAnimator.repeatMode = ValueAnimator.REVERSE  // Reverse the animation after each cycle
        backgroundColorAnimator.repeatCount = ValueAnimator.INFINITE // Infinite repetitions to create continuous effect

        // Apply the background color animation
        backgroundColorAnimator.addUpdateListener { animator ->
            val animatedBackgroundColor = animator.animatedValue as Int
            // Set the background color dynamically
            linearLayout.setBackgroundColor(animatedBackgroundColor)
        }

        // Create the drawable with rounded corners and stroke
        val roundedDrawable = createRoundedDrawable(strokeColor)

        // Set the background with rounded corners and stroke
        linearLayout.background = roundedDrawable

        // Start the background color animation
        backgroundColorAnimator.start()
    }

    fun createRoundedDrawable(strokeColor: Int): LayerDrawable {
        // Create a GradientDrawable with rounded corners and background color
        val roundedCorners = GradientDrawable()
        roundedCorners.cornerRadius = 10f // 10dp corner radius (you can convert dp to pixels if needed)
        roundedCorners.setColor(Color.parseColor("#FFFFFF")) // Background color

        // Convert stroke width from dp to pixels and apply it
        val strokeWidthInPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
        roundedCorners.setStroke(strokeWidthInPx, strokeColor) // Set the stroke with width and color

        // Return the rounded drawable
        return LayerDrawable(arrayOf(roundedCorners))
    }

    protected fun applyRoundedCorners(overlayImage: ImageView) {
        // Get the current drawable from the ImageView
        val drawable = overlayImage.drawable

        if (drawable is BitmapDrawable) {
            // If the drawable is a BitmapDrawable, we can apply rounded corners
            val bitmap = drawable.bitmap
            val roundedDrawable: RoundedBitmapDrawable = RoundedBitmapDrawableFactory.create(overlayImage.resources, bitmap)

            // Set the corner radius for the rounded corners
            roundedDrawable.cornerRadius = 40f  // Adjust the corner radius as needed

            // Set the rounded drawable as the image for the ImageView
            overlayImage.setImageDrawable(roundedDrawable)
        }
    }


    /**
     * Enlarge the overlay image with a breathing effect.
     */
    private fun enlargeImage(view: View, overlayImage: ImageView) {
        // Get the parent view's dimensions (CardView's dimensions or the VideoView's dimensions)
        val parentWidth = (view.parent as View).width
        val parentHeight = (view.parent as View).height

        // Get the original width and height of the overlay image
        val originalWidth = overlayImage.width
        val originalHeight = overlayImage.height

        // Set the enlarged size to match the parent view's dimensions
        val enlargedWidth = parentWidth
        val enlargedHeight = parentHeight

        // Animate the image resize with breathing effect and overlap
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

        // Get the current width and height of the image
        val originalWidth = overlayImage.layoutParams.width
        val originalHeight = overlayImage.layoutParams.height

        // Create a ValueAnimator to animate the size change from the current size to the new smaller size
        val animatorWidth = ValueAnimator.ofInt(originalWidth, widthInPixels)
        val animatorHeight = ValueAnimator.ofInt(originalHeight, widthInPixels)

        // Update listener for width and height
        animatorWidth.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            overlayImage.layoutParams.width = animatedValue
            overlayImage.requestLayout()  // Request layout to apply changes
        }

        animatorHeight.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            overlayImage.layoutParams.height = animatedValue
            overlayImage.requestLayout()  // Request layout to apply changes
        }

        // Set the animation duration (300ms)
        animatorWidth.duration = 300
        animatorHeight.duration = 300

        // Start the animations for both width and height
        animatorWidth.start()
        animatorHeight.start()

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
        // If the image is already enlarged, return early
        if (isEnlarged) {
            // Shrink the image if it's already enlarged
            shrinkImage(overlayImage)
            return
        }

        // Otherwise, proceed with enlarging the image
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

                // Set the flag to indicate the image is enlarged
                isEnlarged = true
            }
        })
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

    // Function to calculate the distance between two geo points
    fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371 // Radius of the Earth in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return earthRadius * c // Distance in km
    }

    // Function to adjust the padding based on the distance between markers
    fun getDynamicPadding(distance: Double): Double {
        return when {
            distance < 0.1 -> 0.0003 // Close proximity, small padding
            distance < 0.5 -> 0.0005 // Medium proximity, slightly more padding
            distance < 1.0 -> 0.002  // Medium proximity, even more padding
            else -> 0.002           // Farther apart, larger padding
        }
    }

    // Move the logic to a function so it can be called in subclasses
    fun adjustMapViewForMarkers(mapView: MapView, firstMarker: Marker, secondMarker: Marker) {
        // Calculate the distance between the two markers
        val distance = calculateDistance(
            firstMarker.position.latitude,
            firstMarker.position.longitude,
            secondMarker.position.latitude,
            secondMarker.position.longitude
        )

        // Adjust the title padding factor based on the distance between the markers
        val titlePaddingFactor = getDynamicPadding(distance)

        // Calculate the center of the bounding box (average of the latitudes and longitudes)
        var extraPaddingForTitle = 0.0 // Adjust this as needed for better title visibility

        if (firstMarker.title == "Sangguniang Bayan Office") {
            // Additional padding for titles, especially if they are near the top or bottom
            extraPaddingForTitle = 0.0005
        } else if (firstMarker.title == "Municipal Administrator Office"
            || firstMarker.title == "Kitaotao Water System Office") {
            extraPaddingForTitle = 0.0003
        } else if (firstMarker.title == "Municipal Tourism Office") {
            extraPaddingForTitle = 0.0001
        }

        val centerLatitude = (firstMarker.position.latitude + secondMarker.position.latitude) / 2
        val centerLongitude = (firstMarker.position.longitude + secondMarker.position.longitude) / 2

        // Adjust bounding box based on marker positions, dynamic title padding, and center
        val adjustedBoundingBox = BoundingBox(
            maxOf(firstMarker.position.latitude, secondMarker.position.latitude) + titlePaddingFactor,
            maxOf(firstMarker.position.longitude, secondMarker.position.longitude) + titlePaddingFactor,
            minOf(firstMarker.position.latitude, secondMarker.position.latitude) - titlePaddingFactor,
            minOf(firstMarker.position.longitude, secondMarker.position.longitude) - titlePaddingFactor
        )

        // Adjust the map to fit the adjusted bounding box with more padding
        mapView.post {
            mapView.zoomToBoundingBox(
                adjustedBoundingBox.increaseByScale(1.2F), // Scale by 20% for more padding
                false // Animated transition
            )
            // Recalculate and update the map center after adjustments
            mapView.controller.setCenter(GeoPoint(centerLatitude, centerLongitude))
        }

        // Add a CompassOverlay
        val compassOverlay = CompassOverlay(mapView.context, mapView)
        compassOverlay.enableCompass()
        mapView.overlays.add(compassOverlay)
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
