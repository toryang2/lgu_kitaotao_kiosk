package com.kitaotao.sst

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.kitaotao.sst.R
import com.kitaotao.sst.office.*

// Function to set dynamic header
fun AppCompatActivity.setDynamicHeader() {
    // Get the root layout of the activity
    val rootLayout = findViewById<FrameLayout>(R.id.rootLayout)

    // Check if the device is a TV or a phone
    val isTv = isTvDevice()

    // Inflate the correct header layout based on the device type
    val headerLayout = if (isTv) {
        // Inflate TV header layout
        LayoutInflater.from(this).inflate(R.layout.header, rootLayout, false)
    } else {
        // Inflate phone header layout
        LayoutInflater.from(this).inflate(R.layout.header_phone, rootLayout, false)
    }

    // Add the header layout to the root layout
    rootLayout.addView(headerLayout)

    // Back Button Logic
    val backButton: Button = headerLayout.findViewById(R.id.buttonBack)
    backButton.let {
        if (isTvDevice()) {
            if (this is MainActivity) {
                it.text = "Exit"
                it.setOnClickListener {
                    showAdminPasswordDialog()
                }
            } else {
                it.setOnClickListener {
                    finish()
                }
            }
        } else {
            if (this is MainActivity) {
                it.text = "Exit"
                it.setOnClickListener {
                    finishAffinity()
                }
            } else {
                it.setOnClickListener {
                    finish()
                }
            }
        }
    }

    // Home Button Logic
    val homeButton: Button = headerLayout.findViewById(R.id.buttonHome)
    homeButton.let {
        if (this is MainActivity
            || this is BidsAndAwardsCommittee
            || this is HumanResourceManagementOffice
            || this is IPMR
            || this is KitaotaoWaterSystem
            || this is LEDIPO
            || this is LIGA
            || this is LYDO
            || this is MDRRMO
            || this is MENRO
            || this is MPDO
            || this is MSWDO
            || this is MunicipalAccountingOffice
            || this is MunicipalAdministratorOffice
            || this is MunicipalAgricultureOffice
            || this is MunicipalAssessorsOffice
            || this is MunicipalBudgetOffice
            || this is MunicipalCivilRegistryOffice
            || this is MunicipalEngineeringOffice
            || this is MunicipalGeneralServiceOffice
            || this is MunicipalHealthOffice
            || this is MunicipalMayorOffice
            || this is PESO
            || this is POPDEV
            || this is PWD
            || this is SBO
            || this is SENIOR
            || this is TOURISM
            || this is TREASURER
            || this is MunicipalBusinessProcessingAndLicensingOffice
            || this is about) {
            it.visibility = View.GONE
        } else {
            it.setOnClickListener {
                // Start MainActivity directly without delay
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Optional: Finish this activity to remove it from the back stack
            }
        }
    }

    // About Button Logic
    val aboutButton: Button = headerLayout.findViewById(R.id.buttonAbout)
    aboutButton.let {
        if (this is MainActivity) {
            it.text = "Settings"
            it.visibility = View.VISIBLE
            it.setOnClickListener {
                // Make sure to create the intent correctly
                val intent = Intent(this, about::class.java)  // Correct class name (AboutActivity in this case)
                startActivity(intent)  // Start the AboutActivity
            }
        } else {
            it.visibility = View.INVISIBLE
        }
    }

    // Back Press Logic (Custom Back Action for MainActivity)
    onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (!isTvDevice()) {
                isEnabled = false // Disable the callback for non-TV devices
                finishAffinity() // Close all activities and exit the app
                return
            }

            if (this@setDynamicHeader is MainActivity) {
                Toast.makeText(this@setDynamicHeader, "Action not allowed!", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    })

}

// Check if the device is a TV
fun AppCompatActivity.isTvDevice(): Boolean {
    return resources.configuration.uiMode and
            android.content.res.Configuration.UI_MODE_TYPE_TELEVISION != 0
}
