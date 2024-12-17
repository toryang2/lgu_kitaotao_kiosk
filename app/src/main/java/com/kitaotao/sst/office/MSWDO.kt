package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.mswdo.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class MSWDO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_mswdo)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.service_1, mswdo_service_1::class.java)
        setClickListener(R.id.service_2, mswdo_service_2::class.java)
        setClickListener(R.id.service_3, mswdo_service_3::class.java)
        setClickListener(R.id.service_4, mswdo_service_4::class.java)
        setClickListener(R.id.service_5, mswdo_service_5::class.java)
        setClickListener(R.id.service_6, mswdo_service_6::class.java)
        setClickListener(R.id.service_7, mswdo_service_7::class.java)
        setClickListener(R.id.service_8, mswdo_service_8::class.java)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = Uri.parse("android.resource://${packageName}/raw/kitaotao_1st_floor_model_mswdo")

        videoView.setVideoURI(videoUri)
        videoView.start() // Automatically start playback

        // Loop the video
        videoView.setOnCompletionListener {
            videoView.start() // Restart video when it finishes
        }

        val textView = findViewById<TextView>(R.id.floorID)
        textView.text = "1st Floor"
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
