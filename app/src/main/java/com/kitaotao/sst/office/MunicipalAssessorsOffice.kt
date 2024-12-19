package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.assessor.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import showClickPopAnimation

class MunicipalAssessorsOffice : BaseActivity() {

    private lateinit var videoView: VideoView
    private lateinit var videoUri: Uri
    private var isVideoPlaying = false // Track video playback state
    private lateinit var overlayImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_assessors_office)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.service_1, assessor_service_1::class.java)
        setClickListener(R.id.service_2, assessor_service_2::class.java)
        setClickListener(R.id.service_3, assessor_service_3::class.java)
        setClickListener(R.id.service_4, assessor_service_4::class.java)
        setClickListener(R.id.service_5, assessor_service_5::class.java)
        setClickListener(R.id.service_6, assessor_service_6::class.java)
        setClickListener(R.id.service_7, assessor_service_7::class.java)
        setClickListener(R.id.service_8, assessor_service_8::class.java)
        setClickListener(R.id.service_9, assessor_service_9::class.java)
        setClickListener(R.id.service_10, assessor_service_10::class.java)
        setClickListener(R.id.service_11, assessor_service_11::class.java)
        setClickListener(R.id.service_12, assessor_service_12::class.java)
        setClickListener(R.id.service_13, assessor_service_13::class.java)

        videoView = findViewById(R.id.videoView)
        videoUri = Uri.parse("android.resource://${packageName}/raw/kitaotao_1st_floor_model_assessor")

        videoView.setVideoURI(videoUri)

        // Loop the video
        videoView.setOnCompletionListener {
            videoView.start() // Restart video when it finishes
        }

        // Start video playback if not already playing
        if (!isVideoPlaying) {
            videoView.start()
            isVideoPlaying = true
        }

        val textView = findViewById<TextView>(R.id.floorID)
        textView.text = "1st Floor"

        // Set the overlay image resource here
        overlayImage = findViewById(R.id.overlayImage) // Ensure you have an ImageView in your layout with this ID
        overlayImage.setImageResource(R.drawable.assessor256)  // Replace with your image resource

        // Set up the overlay image functionality
        setupOverlayImage(videoView, overlayImage)

        // Apply rounded corners without an image
        applyRoundedCorners(overlayImage)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isDeviceTabletClickPop()) {
            showClickPopAnimation(event) // Call the function defined in clickPop.kt
        }
        return super.dispatchTouchEvent(event)
    }

    // Pause the video when the activity is paused
    override fun onPause() {
        super.onPause()
        if (videoView.isPlaying) {
            videoView.pause() // Pause the video to prevent background playback
            isVideoPlaying = false // Track the video state
        }
    }

    // Resume the video when the activity is resumed
    override fun onResume() {
        super.onResume()
        // Restart the video when coming back to this activity
        if (!videoView.isPlaying && !isVideoPlaying) {
            videoView.start() // Start the video again if it was paused
            isVideoPlaying = true
        }
    }


    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
