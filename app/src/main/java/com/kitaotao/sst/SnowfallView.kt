
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.random.Random

class SnowfallView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val snowflakes = mutableListOf<Snowflake>()
    private val snowflakeCount = 100 // Number of snowflakes
    private val paint = Paint().apply { color = Color.WHITE }

    init {
        paint.isAntiAlias = true
    }

    // Initialize snowflakes after the view size is known
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Initialize snowflakes
        snowflakes.clear()
        for (i in 0 until snowflakeCount) {
            snowflakes.add(Snowflake(w.toFloat(), h.toFloat())) // Pass screen dimensions
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw and update snowflakes
        snowflakes.forEach {
            it.update(height.toFloat())
            canvas.drawCircle(it.x, it.y, it.radius, paint)
        }

        // Force continuous redraw for animation
        invalidate()
    }

    // Snowflake data class
    private data class Snowflake(
        var screenWidth: Float,
        var screenHeight: Float,
        var x: Float = Random.nextFloat() * screenWidth, // Random horizontal position
        var y: Float = Random.nextFloat() * screenHeight, // Random starting vertical position
        var radius: Float = Random.nextFloat() * 5 + 2, // Random size (2-7)
        var speed: Float = Random.nextFloat() * 3 + 2, // Random speed (2-5)
    ) {
        fun update(screenHeight: Float) {
            // Move the snowflake downward
            y += speed

            // Reset to the top when the snowflake reaches the bottom
            if (y > screenHeight) {
                y = 0f
                x = Random.nextFloat() * screenWidth // Reset to a new random horizontal position
            }
        }
    }
}