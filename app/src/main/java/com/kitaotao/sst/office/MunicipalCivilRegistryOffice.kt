package com.kitaotao.sst.office

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.MainActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.civilRegistry.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class MunicipalCivilRegistryOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_civil_registry_office)

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.buttonBack, MainActivity::class.java)
        setClickListener(R.id.service_1, civilRegistry_service_1::class.java)
        setClickListener(R.id.service_2, civilRegistry_service_2::class.java)
        setClickListener(R.id.service_3, civilRegistry_service_3::class.java)
        setClickListener(R.id.service_4, civilRegistry_service_4::class.java)
        setClickListener(R.id.service_5, civilRegistry_service_5::class.java)
        setClickListener(R.id.service_6, civilRegistry_service_6::class.java)
        setClickListener(R.id.service_7, civilRegistry_service_7::class.java)
        setClickListener(R.id.service_8, civilRegistry_service_8::class.java)
        setClickListener(R.id.service_9, civilRegistry_service_9::class.java)
        setClickListener(R.id.service_10, civilRegistry_service_10::class.java)
        setClickListener(R.id.service_11, civilRegistry_service_11::class.java)
        setClickListener(R.id.service_12, civilRegistry_service_12::class.java)
        setClickListener(R.id.service_13, civilRegistry_service_13::class.java)

        val videoView = findViewById<VideoView>(R.id.videoView)
        val videoUri = Uri.parse("android.resource://${packageName}/raw/kitaotao_1st_floor_model_civil_registry")

        videoView.setVideoURI(videoUri)
        videoView.start() // Automatically start playback

        // Loop the video
        videoView.setOnCompletionListener {
            videoView.start() // Restart video when it finishes
        }
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
        }
    }
}