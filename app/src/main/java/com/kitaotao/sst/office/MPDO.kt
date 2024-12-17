package com.kitaotao.sst.office

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
import com.kitaotao.sst.services.mpdo.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class MPDO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_mpdo)

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.ex_service_1, mpdo_ex_service_1::class.java)
        setClickListener(R.id.ex_service_2, mpdo_ex_service_2::class.java)
        setClickListener(R.id.ex_service_3, mpdo_ex_service_3::class.java)
        setClickListener(R.id.ex_service_4, mpdo_ex_service_4::class.java)
        setClickListener(R.id.ex_service_5, mpdo_ex_service_5::class.java)
        setClickListener(R.id.in_ex_service_1, mpdo_in_ex_service_1::class.java)
        setClickListener(R.id.in_ex_service_2, mpdo_in_ex_service_2::class.java)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = Uri.parse("android.resource://${packageName}/raw/kitaotao_2st_floor_model_mpdo")

        videoView.setVideoURI(videoUri)
        videoView.start() // Automatically start playback

        // Loop the video
        videoView.setOnCompletionListener {
            videoView.start() // Restart video when it finishes
        }
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
