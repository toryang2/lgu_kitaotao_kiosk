package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.MainActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.services.assessor.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class MunicipalAssessorsOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_assessors_office)

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.buttonBack, MainActivity::class.java)
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
    }
    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
        }
    }
}