import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kitaotao.sst.R
import kotlin.random.Random

class HalloweenFallView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {

    private val pumpkins = mutableListOf<Pumpkin>()
    private val pumpkinCount = 20 // Number of pumpkins
    private val pumpkinBitmaps: List<Bitmap>

    init {
        // Load the pumpkin images into a list
        pumpkinBitmaps = listOf(
            loadBitmap(context, R.drawable.pumpkin1),
            loadBitmap(context, R.drawable.pumpkin2),
            loadBitmap(context, R.drawable.pumpkin3),
            loadBitmap(context, R.drawable.pumpkin4),
            loadBitmap(context, R.drawable.pumpkin5),
            loadBitmap(context, R.drawable.pumpkin6),
            loadBitmap(context, R.drawable.pumpkin7),
            loadBitmap(context, R.drawable.pumpkin8),
            loadBitmap(context, R.drawable.pumpkin9),
        )
    }

    private fun loadBitmap(context: Context, drawableRes: Int): Bitmap {
        val drawable = context.getDrawable(drawableRes)
        val tempBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(tempBitmap)
        drawable?.setBounds(0, 0, 100, 100)
        drawable?.draw(canvas)
        return tempBitmap
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        // Initialize pumpkins
        pumpkins.clear()
        for (i in 0 until pumpkinCount) {
            val randomBitmap = pumpkinBitmaps.random()
            val sizeFactor = Random.nextFloat() * 0.2f + 0.2f // Random size (50% to 100%)
            val scaledBitmap = Bitmap.createScaledBitmap(
                randomBitmap,
                (randomBitmap.width * sizeFactor).toInt(),
                (randomBitmap.height * sizeFactor).toInt(),
                true
            )
            pumpkins.add(Pumpkin(
                screenWidth = w.toFloat(),
                screenHeight = h.toFloat(),
                bitmap = scaledBitmap,
                sizeFactor = sizeFactor
            ))
        }
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw and update pumpkins
        pumpkins.forEach {
            it.update(height.toFloat()) // Update position and rotation
            canvas.save()

            // Apply the rotation
            canvas.rotate(it.rotation, it.x, it.y)

            // Draw the pumpkin image
            val pumpkinBitmap = it.bitmap
            canvas.drawBitmap(pumpkinBitmap, it.x - pumpkinBitmap.width / 2, it.y - pumpkinBitmap.height / 2, null)

            canvas.restore()
        }

        // Force continuous redraw for animation
        invalidate()
    }

    private data class Pumpkin(
        var screenWidth: Float,
        var screenHeight: Float,
        var x: Float = Random.nextFloat() * screenWidth, // Random horizontal position
        var y: Float = Random.nextFloat() * screenHeight, // Random vertical position
        var speed: Float = Random.nextFloat() * 3 + 2, // Random speed (2-5)
        var rotation: Float = Random.nextFloat() * 360, // Initial random rotation angle (0-360)
        val bitmap: Bitmap, // Randomly selected pumpkin image
        val sizeFactor: Float = Random.nextFloat() * 0.2f + 0.2f // Random scale factor (50% to 100%)
    ) {
        fun update(screenHeight: Float) {
            // Move the pumpkin downward
            y += speed
            rotation += 2 // Rotate by 2 degrees per frame

            // Reset when it reaches the bottom
            if (y > screenHeight) {
                y = 0f
                x = Random.nextFloat() * screenWidth
                rotation = Random.nextFloat() * 360
            }
        }
    }
}
