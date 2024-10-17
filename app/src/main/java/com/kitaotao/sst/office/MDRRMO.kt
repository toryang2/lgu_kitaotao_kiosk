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
import com.kitaotao.sst.services.mdrrmo.external.*
import com.kitaotao.sst.services.mdrrmo.internal_external.*

class MDRRMO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_mdrrmo)
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
        setClickListener(R.id.ex_service_1, mdrrmo_ex_service_1::class.java)
        setClickListener(R.id.inEx_service_1, mdrrmo_in_ex_service_1::class.java)
        setClickListener(R.id.inEx_service_2, mdrrmo_in_ex_service_2::class.java)
        setClickListener(R.id.inEx_service_3, mdrrmo_in_ex_service_3::class.java)
        setClickListener(R.id.inEx_service_4, mdrrmo_in_ex_service_4::class.java)
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
