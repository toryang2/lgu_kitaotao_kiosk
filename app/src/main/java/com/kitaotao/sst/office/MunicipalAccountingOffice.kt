package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.accounting.accounting_service_1
import com.kitaotao.sst.services.accounting.accounting_service_2
import com.kitaotao.sst.services.accounting.accounting_service_3
import com.kitaotao.sst.services.accounting.accounting_service_4


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
        val accountingService1 = findViewById<TextView>(R.id.service_1)
        accountingService1.setOnClickListener{
            val intent = Intent(this, accounting_service_1::class.java)
            startActivity(intent)
        }
        val accountingService2 = findViewById<TextView>(R.id.service_2)
        accountingService2.setOnClickListener{
            val intent = Intent(this, accounting_service_2::class.java)
            startActivity(intent)
        }
        val accountingService3 = findViewById<TextView>(R.id.service_3)
        accountingService3.setOnClickListener{
            val intent = Intent(this, accounting_service_3::class.java)
            startActivity(intent)
        }
        val accountingService4 = findViewById<TextView>(R.id.service_4)
        accountingService4.setOnClickListener{
            val intent = Intent(this, accounting_service_4::class.java)
            startActivity(intent)
        }
    }
}