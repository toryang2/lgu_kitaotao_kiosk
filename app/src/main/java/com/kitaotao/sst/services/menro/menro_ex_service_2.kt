package com.kitaotao.sst.services.menro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.MainActivity
import com.kitaotao.sst.R

class menro_ex_service_2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.menro_ex_service2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //Back Button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })

        val backButton: Button = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }
        //end of back button
        val homeButton: Button = findViewById(R.id.buttonHome)
        homeButton.setOnClickListener {
            // Start MainActivity directly without delay
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Optional: Finish this activity to remove it from the back stack
        }
    }
}