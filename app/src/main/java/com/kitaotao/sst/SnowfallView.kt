import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class SnowfallView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val snowflakes = mutableListOf<Snowflake>()
    private val snowflakeCount = 150 // Total number of snowflakes
    private val paint = Paint().apply { isAntiAlias = true }

    init {
        paint.color = Color.WHITE // Default snowflake color
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Initialize snowflakes
        snowflakes.clear()
        for (i in 0 until snowflakeCount) {
            snowflakes.add(Snowflake(w.toFloat(), h.toFloat()))
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw and update each snowflake
        snowflakes.forEach { snowflake ->
            paint.alpha = (snowflake.opacity * 255).toInt() // Adjust opacity
            canvas.drawCircle(snowflake.x, snowflake.y, snowflake.radius, paint)
            snowflake.update(width.toFloat(), height.toFloat())
        }

        // Continuously redraw the view for animation
        invalidate()
    }

    private data class Snowflake(
        var screenWidth: Float,
        var screenHeight: Float,
        var x: Float = Random.nextFloat() * screenWidth,
        var y: Float = Random.nextFloat() * screenHeight,
        var radius: Float = Random.nextFloat() * 8 + 2, // Random size (2-10)
        var speed: Float = Random.nextFloat() * 2 + 1, // Random downward speed (1-3)
        var drift: Float = Random.nextFloat() * 0.5f - 0.25f, // Random horizontal drift (-0.25 to 0.25)
        var opacity: Float = Random.nextFloat() * 0.5f + 0.5f, // Random opacity (0.5 to 1)
        var angle: Float = Random.nextFloat() * 360 // Random angle for smooth drift
    ) {
        fun update(screenWidth: Float, screenHeight: Float) {
            // Move the snowflake downward
            y += speed
            x += drift + sin(angle) * 0.5f // Add horizontal drift and slight sinusoidal motion

            // Update angle for smooth drifting
            angle += 0.1f
            if (angle > 360) angle = 0f

            // Reset position when snowflake moves out of bounds
            if (y > screenHeight) {
                y = 0f
                x = Random.nextFloat() * screenWidth
            }

            // Wrap around horizontally
            if (x < 0) x = screenWidth
            if (x > screenWidth) x = 0f
        }
    }
}
