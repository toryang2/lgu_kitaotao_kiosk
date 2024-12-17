package com.kitaotao.sst.office

import addSeasonalBackground
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.mdrrmo.external.*
import com.kitaotao.sst.services.mdrrmo.internal_external.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class MDRRMO : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_mdrrmo)

        addSeasonalBackground()

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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
