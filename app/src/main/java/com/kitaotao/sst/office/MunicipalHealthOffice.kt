package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.MainActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.mho.*

class MunicipalHealthOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_health_office)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        setClickListener(R.id.service_1, mho_service_1::class.java)
        setClickListener(R.id.service_2, mho_service_2::class.java)
        setClickListener(R.id.service_3, mho_service_3::class.java)
        setClickListener(R.id.service_4, mho_service_4::class.java)
        setClickListener(R.id.service_5, mho_service_5::class.java)
        setClickListener(R.id.service_6, mho_service_6::class.java)
        setClickListener(R.id.service_7, mho_service_7::class.java)
        setClickListener(R.id.service_8, mho_service_8::class.java)
        setClickListener(R.id.service_9, mho_service_9::class.java)
        setClickListener(R.id.service_10, mho_service_10::class.java)
        setClickListener(R.id.service_11, mho_service_11::class.java)
        setClickListener(R.id.service_12, mho_service_12::class.java)
        setClickListener(R.id.service_13, mho_service_13::class.java)
        setClickListener(R.id.service_14, mho_service_14::class.java)
        setClickListener(R.id.service_15, mho_service_15::class.java)
        setClickListener(R.id.service_16, mho_service_16::class.java)


    }
    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
        }
    }
}