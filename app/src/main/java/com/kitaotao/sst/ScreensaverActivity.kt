package com.kitaotao.sst

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ScreensaverActivity : AppCompatActivity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screensaver)

        val videoView: VideoView = findViewById(R.id.screensaverVideo)
        val videoUri = Uri.parse("android.resource://${packageName}/raw/kitakitz_kitaotao_palanggaon_ko_ikaw")  // Replace with your video URI
        videoView.setVideoURI(videoUri)

        videoView.setOnPreparedListener { mediaPlayer ->
            // Adjust the volume to 10%
            val volumeLevel = 0.1f // 10%
            mediaPlayer.setVolume(volumeLevel, volumeLevel)

            // Ensure the video is scaled to fill the screen by adjusting the layout parameters
            val videoWidth = mediaPlayer.videoWidth
            val videoHeight = mediaPlayer.videoHeight
            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            // Calculate scale ratio and adjust layout accordingly
            val scaleX = screenWidth.toFloat() / videoWidth
            val scaleY = screenHeight.toFloat() / videoHeight
            val scale = maxOf(scaleX, scaleY)

            // Adjust the `VideoView` size to fill the screen (crop if necessary)
            videoView.layoutParams.width = (videoWidth * scale).toInt()
            videoView.layoutParams.height = (videoHeight * scale).toInt()

            // Apply the new layout parameters
            videoView.requestLayout()
        }

        // Set touch listener to navigate back to the PostScreen on tap
        videoView.setOnTouchListener { _, _ ->
            val intent = Intent(this, postScreen::class.java)
            startActivity(intent)
            finish()
            true
        }

        // Loop the video
        videoView.setOnCompletionListener {
            videoView.start()
        }

        // Start the video
        videoView.start()
    }

    override fun onPause() {
        super.onPause()
        val videoView: VideoView = findViewById(R.id.screensaverVideo)
        if (videoView.isPlaying) {
            videoView.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        val videoView: VideoView = findViewById(R.id.screensaverVideo)
        if (!videoView.isPlaying) {
            videoView.start()
        }
    }
}