package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MotionEvent
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.hrmo.internal.*
import com.kitaotao.sst.services.hrmo.internal_external.*
import com.kitaotao.sst.setDynamicHeader
import isDeviceTabletClickPop
import officeViewChange
import showClickPopAnimation

class HumanResourceManagementOffice : BaseActivity() {

    private lateinit var videoView: VideoView
    private lateinit var videoUri: Uri
    private var isVideoPlaying = false // Track video playback state

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_human_resource_management_office)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.int_service_1, hrmo_int_service_1::class.java)
        setClickListener(R.id.int_service_2, hrmo_int_service_2::class.java)
        setClickListener(R.id.int_service_3, hrmo_int_service_3::class.java)
        setClickListener(R.id.int_service_4, hrmo_int_service_4::class.java)
        setClickListener(R.id.inEx_service_1, hrmo_int_ext_service_1::class.java)
        setClickListener(R.id.inEx_service_2, hrmo_int_ext_service_2::class.java)

        videoView = findViewById(R.id.videoView)
        videoUri = Uri.parse("android.resource://${packageName}/raw/kitaotao_2nd_floor_model_hr")

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
        textView.text = "2nd Floor"
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
