package com.kitaotao.sst.services.civilRegistry

import addSeasonalBackground
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.BaseActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.setDynamicHeader

class civilRegistry_service_7 : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.civil_registry_service7)

        addSeasonalBackground()

        setDynamicHeader()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}