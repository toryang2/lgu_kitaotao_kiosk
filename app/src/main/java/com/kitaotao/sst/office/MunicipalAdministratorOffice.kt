package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.administrator.administrator_service_1
import com.kitaotao.sst.services.administrator.administrator_service_2
import org.w3c.dom.Text

class MunicipalAdministratorOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_municipal_administrator_office)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val administratorService1 = findViewById<TextView>(R.id.service_1)
        administratorService1.setOnClickListener {
            val intent = Intent(this, administrator_service_1::class.java)
            startActivity(intent)
        }

        val administratorService2 = findViewById<TextView>(R.id.service_2)
        administratorService2.setOnClickListener {
            val intent = Intent(this, administrator_service_2::class.java)
            startActivity(intent)
        }
    }
}