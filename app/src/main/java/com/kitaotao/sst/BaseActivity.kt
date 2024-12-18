package com.kitaotao.sst

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

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
