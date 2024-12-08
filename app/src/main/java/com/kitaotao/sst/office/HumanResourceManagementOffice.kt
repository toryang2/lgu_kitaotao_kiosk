package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.hrmo.internal.*
import com.kitaotao.sst.services.hrmo.internal_external.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class HumanResourceManagementOffice : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_human_resource_management_office)

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        setClickListener(R.id.int_service_1, hrmo_int_service_1::class.java)
        setClickListener(R.id.int_service_2, hrmo_int_service_2::class.java)
        setClickListener(R.id.int_service_3, hrmo_int_service_3::class.java)
        setClickListener(R.id.int_service_4, hrmo_int_service_4::class.java)
        setClickListener(R.id.inEx_service_1, hrmo_int_ext_service_1::class.java)
        setClickListener(R.id.inEx_service_2, hrmo_int_ext_service_2::class.java)
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
