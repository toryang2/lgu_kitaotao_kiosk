package com.kitaotao.sst

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import showClickPopAnimation
import java.io.FileNotFoundException

class ChangePasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        setDynamicHeader()

        val dummyView = findViewById<View>(R.id.dummyView)
        dummyView.requestFocus()

        val oldPasswordEditText = findViewById<EditText>(R.id.editTextOldPassword)
        val newPasswordEditText = findViewById<EditText>(R.id.editTextNewPassword)
        val confirmPasswordEditText = findViewById<EditText>(R.id.editTextConfirmNewPassword)
        val saveButton = findViewById<Button>(R.id.buttonSavePassword)
        val resetButton = findViewById<Button>(R.id.buttonResetPassword)

        // Handle Save Button Logic
        val savePasswordHandler = {
            val oldPassword = oldPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (oldPassword != readPassword()) {
                Toast.makeText(this, "Incorrect old password", Toast.LENGTH_SHORT).show()
            } else if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            } else {
                saveNewPassword(newPassword)
                showSuccessDialog()
            }
        }

        // Trigger Save Button on Enter Key
        listOf(oldPasswordEditText, newPasswordEditText, confirmPasswordEditText).forEach { editText ->
            editText.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    savePasswordHandler()
                    true
                } else {
                    false
                }
            }
        }

        // Save Button Click Listener
        saveButton.setOnClickListener {
            savePasswordHandler()
        }

        // Reset Password Button
        resetButton.setOnClickListener {
            resetPassword()
            Toast.makeText(this, "Password reset to default", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveNewPassword(password: String) {
        openFileOutput("admin_password.txt", MODE_PRIVATE).use {
            it.write(password.toByteArray())
        }
    }

    private fun readPassword(): String {
        return try {
            openFileInput("admin_password.txt").bufferedReader().use { it.readText() }
        } catch (e: FileNotFoundException) {
            "adminkitaotao" // Default password
        }
    }

    private fun resetPassword() {
        saveNewPassword("adminkitaotao")
    }

    private fun showSuccessDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("Password has been changed successfully.")
            .setPositiveButton("OK") { _, _ ->
                navigateBackToAboutPage()
            }
            .setCancelable(false)
            .create()

        // Show the dialog first
        dialog.show()

        // Set the custom color for the positive button text using Color.parseColor
        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
    }

    private fun navigateBackToAboutPage() {
        val intent = Intent(this, about::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isTvDevice()) {
            showClickPopAnimation(event) // Call the function defined in clickPop.kt
        }
        return super.dispatchTouchEvent(event)
    }
}
