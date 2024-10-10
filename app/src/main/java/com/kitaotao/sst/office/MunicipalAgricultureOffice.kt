package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.MainActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.agriculture.*

class MunicipalAgricultureOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_agriculture_office)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Back Button implementation
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish() // Finish current activity when back is pressed
            }
        })

        val backButton: Button = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish() // Finish current activity when back button is clicked
        }

        // Home Button implementation
        val homeButton: Button = findViewById(R.id.buttonHome)
        homeButton.setOnClickListener {
            // Start MainActivity directly without delay
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish this activity to remove it from the back stack
        }

        // Set click listeners for various services
        setClickListener(R.id.buttonBack, MainActivity::class.java)
        setClickListener(R.id.service_1, agriculture_service_1::class.java)
        setClickListener(R.id.service_2, agriculture_service_2::class.java)
        setClickListener(R.id.service_3, agriculture_service_3::class.java)
        setClickListener(R.id.service_4, agriculture_service_4::class.java)
        setClickListener(R.id.service_5, agriculture_service_5::class.java)
        setClickListener(R.id.service_6, agriculture_service_6::class.java)
        setClickListener(R.id.service_7, agriculture_service_7::class.java)
        setClickListener(R.id.service_8, agriculture_service_8::class.java)
        setClickListener(R.id.service_9, agriculture_service_9::class.java)
        setClickListener(R.id.service_10, agriculture_service_10::class.java)
        setClickListener(R.id.service_11, agriculture_service_11::class.java)
        setClickListener(R.id.service_12, agriculture_service_12::class.java)
        setClickListener(R.id.service_13, agriculture_service_13::class.java)
    }
    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
        }
    }
}