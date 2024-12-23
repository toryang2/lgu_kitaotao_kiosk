import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class HeartFallView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val hearts = mutableListOf<Heart>()
    private val heartCount = 100 // Number of hearts
    private val paint = Paint().apply { color = Color.rgb(255, 105, 180) } // Pink color

    init {
        paint.isAntiAlias = true
    }

    // Initialize hearts after the view size is known
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Initialize hearts
        hearts.clear()
        for (i in 0 until heartCount) {
            hearts.add(Heart(w.toFloat(), h.toFloat())) // Pass screen dimensions
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw and update hearts
        hearts.forEach {
            it.update(height.toFloat()) // Update position and rotation
            canvas.save()

            // Apply the rotation
            canvas.rotate(it.rotation, it.x, it.y)

            // Draw the heart
            canvas.drawPath(it.getHeartPath(), paint)

            // Restore the canvas after drawing the heart
            canvas.restore()
        }

        // Force continuous redraw for animation
        invalidate()
    }

    // Heart data class
    private data class Heart(
        var screenWidth: Float,
        var screenHeight: Float,
        var x: Float = Random.nextFloat() * screenWidth, // Random horizontal position
        var y: Float = Random.nextFloat() * screenHeight, // Random starting vertical position
        var radius: Float = Random.nextFloat() * 10 + 5, // Increased size range (5-15)
        var speed: Float = Random.nextFloat() * 3 + 2, // Random speed (2-5)
        var rotation: Float = Random.nextFloat() * 360 // Initial random rotation angle (0-360)
    ) {
        fun update(screenHeight: Float) {
            // Move the heart downward
            y += speed
            rotation += 2 // Continuously rotate by 1 degree per frame

            // Reset rotation when it completes a full circle (optional)
            if (rotation >= 360) {
                rotation = 0f
            }

            // Reset to the top when the heart reaches the bottom
            if (y > screenHeight) {
                y = 0f
                x = Random.nextFloat() * screenWidth // Reset to a new random horizontal position
                rotation = Random.nextFloat() * 360 // Random initial rotation when it resets
            }
        }

        // Create a heart-shaped path based on the size of the heart
        fun getHeartPath(): Path {
            val path = Path()
            val heartSize = radius * 1.5f // Adjust heart size based on radius

            val cx = x
            val cy = y
            path.moveTo(cx, cy)

            // Draw heart shape using cubic Bezier curves
            path.cubicTo(cx - heartSize / 2, cy - heartSize / 2, cx - heartSize, cy + heartSize / 2, cx, cy + heartSize)
            path.cubicTo(cx + heartSize, cy + heartSize / 2, cx + heartSize / 2, cy - heartSize / 2, cx, cy)

            return path
        }
    }
}
