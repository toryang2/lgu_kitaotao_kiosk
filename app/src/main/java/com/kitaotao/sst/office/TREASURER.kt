package com.kitaotao.sst.office

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.R
import com.kitaotao.sst.services.treasurer.internal.*
import com.kitaotao.sst.services.treasurer.external.*
import com.kitaotao.sst.services.treasurer.internal_external.*
import com.kitaotao.sst.setDynamicHeader
import officeViewChange

class TREASURER : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.office_treasurer)

        setDynamicHeader()

        officeViewChange()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Set click listeners for various services
        //External Services
        setClickListener(R.id.ex_service_1, mto_ex_service_1::class.java)
        setClickListener(R.id.ex_service_2, mto_ex_service_2::class.java)
        setClickListener(R.id.ex_service_3, mto_ex_service_3::class.java)
        setClickListener(R.id.ex_service_4, mto_ex_service_4::class.java)
        setClickListener(R.id.ex_service_5, mto_ex_service_5::class.java)
        setClickListener(R.id.ex_service_6, mto_ex_service_6::class.java)
        setClickListener(R.id.ex_service_7, mto_ex_service_7::class.java)
        //Internal Services
        setClickListener(R.id.in_service_1, mto_in_service_1::class.java)
        setClickListener(R.id.in_service_2, mto_in_service_2::class.java)
        setClickListener(R.id.in_service_3, mto_in_service_3::class.java)
        //Internal/External Services
        setClickListener(R.id.in_ex_service_1, mto_in_ex_service_1::class.java)
        setClickListener(R.id.in_ex_service_2, mto_in_ex_service_2::class.java)
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
