package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.agriculture.agriculture_service_1
import com.kitaotao.sst.services.agriculture.agriculture_service_10
import com.kitaotao.sst.services.agriculture.agriculture_service_11
import com.kitaotao.sst.services.agriculture.agriculture_service_12
import com.kitaotao.sst.services.agriculture.agriculture_service_13
import com.kitaotao.sst.services.agriculture.agriculture_service_2
import com.kitaotao.sst.services.agriculture.agriculture_service_3
import com.kitaotao.sst.services.agriculture.agriculture_service_4
import com.kitaotao.sst.services.agriculture.agriculture_service_5
import com.kitaotao.sst.services.agriculture.agriculture_service_6
import com.kitaotao.sst.services.agriculture.agriculture_service_7
import com.kitaotao.sst.services.agriculture.agriculture_service_8
import com.kitaotao.sst.services.agriculture.agriculture_service_9

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

        val agricultureService1 = findViewById<TextView>(R.id.service_1)
        agricultureService1.setOnClickListener {
            val intent = Intent(this, agriculture_service_1::class.java)
            startActivity(intent)
        }

        val agricultureService2 = findViewById<TextView>(R.id.service_2)
        agricultureService2.setOnClickListener {
            val intent = Intent(this, agriculture_service_2::class.java)
            startActivity(intent)
        }

        val agricultureService3 = findViewById<TextView>(R.id.service_3)
        agricultureService3.setOnClickListener {
            val intent = Intent(this, agriculture_service_3::class.java)
            startActivity(intent)
        }

        val agricultureService4 = findViewById<TextView>(R.id.service_4)
        agricultureService4.setOnClickListener {
            val intent = Intent(this, agriculture_service_4::class.java)
            startActivity(intent)
        }

        val agricultureService5 = findViewById<TextView>(R.id.service_5)
        agricultureService5.setOnClickListener {
            val intent = Intent(this, agriculture_service_5::class.java)
            startActivity(intent)
        }

        val agricultureService6 = findViewById<TextView>(R.id.service_6)
        agricultureService6.setOnClickListener {
            val intent = Intent(this, agriculture_service_6::class.java)
            startActivity(intent)
        }

        val agricultureService7 = findViewById<TextView>(R.id.service_7)
        agricultureService7.setOnClickListener {
            val intent = Intent(this, agriculture_service_7::class.java)
            startActivity(intent)
        }

        val agricultureService8 = findViewById<TextView>(R.id.service_8)
        agricultureService8.setOnClickListener {
            val intent = Intent(this, agriculture_service_8::class.java)
            startActivity(intent)
        }

        val agricultureService9 = findViewById<TextView>(R.id.service_9)
        agricultureService9.setOnClickListener {
            val intent = Intent(this, agriculture_service_9::class.java)
            startActivity(intent)
        }

        val agricultureService10 = findViewById<TextView>(R.id.service_10)
        agricultureService10.setOnClickListener {
            val intent = Intent(this, agriculture_service_10::class.java)
            startActivity(intent)
        }

        val agricultureService11 = findViewById<TextView>(R.id.service_11)
        agricultureService11.setOnClickListener {
            val intent = Intent(this, agriculture_service_11::class.java)
            startActivity(intent)
        }

        val agricultureService12 = findViewById<TextView>(R.id.service_12)
        agricultureService12.setOnClickListener {
            val intent = Intent(this, agriculture_service_12::class.java)
            startActivity(intent)
        }

        val agricultureService13 = findViewById<TextView>(R.id.service_13)
        agricultureService13.setOnClickListener {
            val intent = Intent(this, agriculture_service_13::class.java)
            startActivity(intent)
        }

    }
}