package com.kitaotao.sst

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.io.FileNotFoundException

class about : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.about)

        // Make sure your layout has a main parent with the id "main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val versionTextView: TextView = findViewById(R.id.versionTextView)
        versionTextView.text = "v${BuildConfig.VERSION_NAME}"

        // Back Button
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Add logs here to check whether this callback is being triggered correctly
                Log.d("AboutActivity", "Back button pressed, finishing activity.")
                finish()
            }
        })

        val backButton: Button = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish() // Finish on back button click
        }

        listOf(R.id.buttonAbout, R.id.buttonHome).forEach { buttonId ->
            val button = findViewById<Button>(buttonId)
            button.visibility = View.GONE
        }

        val changePasswordButton: Button = findViewById(R.id.buttonChangePassword)
        changePasswordButton.setOnClickListener {
            showPasswordConfirmationDialog()
        }

        // Reset Password Button Logic
        val resetPasswordButton: Button = findViewById(R.id.buttonResetPassword)
        resetPasswordButton.setOnClickListener {
            resetPassword()
            Toast.makeText(this, "Password reset to default", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showPasswordConfirmationDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_password, null)
        val passwordEditText: EditText = dialogView.findViewById(R.id.passwordEditText)

        // Create and show the dialog
        val dialog = AlertDialog.Builder(this)
            .setTitle("Admin Password")
            .setMessage("Please enter the admin password to proceed.")
            .setView(dialogView)
            .setPositiveButton("OK", null) // Set to null to handle manually
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            val color = Color.parseColor("#49454F")
            positiveButton.setTextColor(color)
            negativeButton.setTextColor(color)

            positiveButton.setOnClickListener {
                val enteredPassword = passwordEditText.text.toString()
                val storedPassword = readPassword()

                if (enteredPassword == storedPassword) {
                    navigateToChangePassword()
                    dialog.dismiss()
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Handle Enter key to trigger the OK button
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).performClick()
                true
            } else {
                false
            }
        }

        dialog.show()
    }

    private fun resetPassword() {
        try {
            saveNewPassword("adminkitaotao") // Resetting to default password
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to reset password", Toast.LENGTH_SHORT).show()
            e.printStackTrace()  // For debugging
        }
    }

    private fun saveNewPassword(password: String) {
        try {
            openFileOutput("admin_password.txt", MODE_PRIVATE).use {
                it.write(password.toByteArray())
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving new password", Toast.LENGTH_SHORT).show()
            e.printStackTrace()  // For debugging
        }
    }

    private fun readPassword(): String {
        return try {
            openFileInput("admin_password.txt").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            "adminkitaotao" // Default password
        } catch (e: Exception) {
            Toast.makeText(this, "Error reading password", Toast.LENGTH_SHORT).show()
            e.printStackTrace()  // For debugging
            "adminkitaotao" // Default password
        }
    }

    private fun navigateToChangePassword() {
        val intent = Intent(this, ChangePasswordActivity::class.java)
        startActivity(intent)
    }
}