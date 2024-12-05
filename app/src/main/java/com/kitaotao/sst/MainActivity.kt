package com.kitaotao.sst

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitaotao.sst.office.*
import java.io.FileNotFoundException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up RecyclerView
        setupRecyclerView()

        // Handle back press behavior
        handleBackPress()
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Sample data for the grid items, now including target activities
        val items = listOf(
            GridItem("Municipal Accounting Office", getDrawable(R.drawable.macco256)!!, MunicipalAccountingOffice::class.java),
            GridItem("Municipal Administrator Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalAdministratorOffice::class.java),
            GridItem("Municipal Agriculture Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalAgricultureOffice::class.java),
            GridItem("Municipal Assessor's Office", getDrawable(R.drawable.assessor256)!!, MunicipalAssessorsOffice::class.java),
            GridItem("Municipal Budget Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalBudgetOffice::class.java),
            GridItem("Municipal Business Processing and Licensing Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalBusinessProcessingAndLicensingOffice::class.java),
            GridItem("Municipal Civil Registry Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalCivilRegistryOffice::class.java),
            GridItem("Municipal Disaster Risk Reduction Management Office", getDrawable(R.drawable.mdrrmc256)!!, MDRRMO::class.java),
            GridItem("Municipal Engineering Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalEngineeringOffice::class.java),
            GridItem("Municipal Environmental and Natural Resources Office", getDrawable(R.drawable.postscreenlogo256)!!, MENRO::class.java),
            GridItem("Municipal General Services Office", getDrawable(R.drawable.gso256)!!, MunicipalGeneralServiceOffice::class.java),
            GridItem("Municipal Health Office", getDrawable(R.drawable.mho256)!!, MunicipalHealthOffice::class.java),
            GridItem("Human Resource Management Office", getDrawable(R.drawable.postscreenlogo256)!!, HumanResourceManagementOffice::class.java),
            GridItem("Municipal Mayor's Office", getDrawable(R.drawable.postscreenlogo256)!!, MunicipalMayorOffice::class.java),
            GridItem("Bids and Awards Committee", getDrawable(R.drawable.postscreenlogo256)!!, BidsAndAwardsCommittee::class.java),
            GridItem("Indigineous People Mandatory Representative", getDrawable(R.drawable.postscreenlogo256)!!, IPMR::class.java),
            GridItem("Kitaotao Water System Office", getDrawable(R.drawable.watersystem256)!!, KitaotaoWaterSystem::class.java),
            GridItem("LIGA ng Barangay", getDrawable(R.drawable.postscreenlogo256)!!, LIGA::class.java),
            GridItem("Local Economic Development and Investment Promotion Office", getDrawable(R.drawable.ledipo256)!!, LEDIPO::class.java),
            GridItem("Local Youth Development Office", getDrawable(R.drawable.postscreenlogo256)!!, LYDO::class.java),
            GridItem("Municipal Tourism Office", getDrawable(R.drawable.tourism256)!!, TOURISM::class.java),
            GridItem("Person With Disability Office", getDrawable(R.drawable.postscreenlogo256)!!, PWD::class.java),
            GridItem("Public Employment Services Office", getDrawable(R.drawable.postscreenlogo256)!!, PESO::class.java),
            GridItem("Office of the Senior Citizen's Affairs", getDrawable(R.drawable.postscreenlogo256)!!, SENIOR::class.java),
            GridItem("Municipal Planning and Development Office", getDrawable(R.drawable.postscreenlogo256)!!, MPDO::class.java),
            GridItem("Population Development Office", getDrawable(R.drawable.postscreenlogo256)!!, POPDEV::class.java),
            GridItem("Sangguniang Bayan Office", getDrawable(R.drawable.postscreenlogo256)!!, SBO::class.java),
            GridItem("Municipal Social Welfare and Development Office", getDrawable(R.drawable.postscreenlogo256)!!, MSWDO::class.java),
            GridItem("Municipal Treasurer's Office", getDrawable(R.drawable.treasury256)!!, TREASURER::class.java)
        )

        // Set up the grid layout manager and adapter
        val layoutManager = GridLayoutManager(this, 10)
        recyclerView.layoutManager = layoutManager

        // Set the adapter with context passed for item click handling
        val adapter = CardAdapter(items, this)
        recyclerView.adapter = adapter

        val aboutButton: Button = findViewById(R.id.buttonAbout)

        aboutButton.visibility = View.VISIBLE

        aboutButton.setOnClickListener {
            val intent = Intent(this, about::class.java)
            startActivity(intent)
        }

        listOf(R.id.buttonBack, R.id.buttonHome).forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.visibility = View.GONE

            if (buttonId == R.id.buttonBack) {
                button.visibility = View.VISIBLE

                button.text = "Exit"

                button.setOnClickListener{
                    showAdminPasswordDialog()
                }
            }
        }
    }

    private fun handleBackPress() {
        // This will intercept back button presses and prevent the default behavior
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Prevent back press and show a message
                Toast.makeText(this@MainActivity, "Action not allowed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Intercept Home button press (KEYCODE_HOME)
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            showAdminPasswordDialog()
            return true  // Consume the event
        }

        // Intercept Overview button press (KEYCODE_APP_SWITCH)
        if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            showAdminPasswordDialog()
            return true  // Consume the event
        }

        return super.onKeyDown(keyCode, event)
    }

    private fun showAdminPasswordDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_password, null)
        val passwordEditText: EditText = dialogView.findViewById(R.id.passwordEditText)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Admin Password")
            .setMessage("Please enter the admin password to exit Kiosk Mode.")
            .setView(dialogView)
            .setPositiveButton("OK", null) // Set to null to prevent automatic dismissal
            .setNegativeButton("Cancel", null)
            .create()

        // Set the action for the OK button manually to verify the password without dismissing
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            // Set the color for OK and Cancel buttons
            val color = Color.parseColor("#49454F")
            positiveButton.setTextColor(color)
            negativeButton.setTextColor(color)

            // Set the action when OK button is clicked
            positiveButton.setOnClickListener {
                val enteredPassword = passwordEditText.text.toString()
                val storedPassword = readPassword() // Retrieve stored password
                if (enteredPassword == storedPassword) {
                    exitKioskMode() // Exit Kiosk Mode if password is correct
                    dialog.dismiss() // Close the dialog after correct password
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle Enter key press to trigger the OK button click
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // When "Enter" is pressed, trigger the OK button click
                val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.performClick()
                true // Indicate that the action was handled
            } else {
                false
            }
        }
        dialog.show()
    }


    // Read the password from the file
    private fun readPassword(): String {
        return try {
            openFileInput("admin_password.txt").bufferedReader().use {
                it.readText()
            }
        } catch (e: FileNotFoundException) {
            "adminkitaotao"  // Return default password if not set
        }
    }



    // Exit Kiosk Mode and stop the lock task
    private fun exitKioskMode() {
        stopLockTask()

        Toast.makeText(this, "Exited Kiosk Mode", Toast.LENGTH_SHORT).show()

        finishAffinity()
    }
}