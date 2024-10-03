package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.accounting.*


class MunicipalAccountingOffice : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_accounting_office)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setClickListener(R.id.service_1, accounting_service_1::class.java)
        setClickListener(R.id.service_2, accounting_service_2::class.java)
        setClickListener(R.id.service_3, accounting_service_3::class.java)
        setClickListener(R.id.service_4, accounting_service_4::class.java)
        setClickListener(R.id.service_5, accounting_service_5::class.java)
        setClickListener(R.id.service_6, accounting_service_6::class.java)
        setClickListener(R.id.service_7, accounting_service_7::class.java)
    }
    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
        }
    }
}