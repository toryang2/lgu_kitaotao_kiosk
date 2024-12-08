package com.kitaotao.sst

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.kitaotao.sst.network.GitHubService
import com.kitaotao.sst.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class about : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.about)

        setDynamicHeader()

        val githubProfilePicture: ImageView = findViewById(R.id.githubProfilePicture)
        val githubLink: TextView = findViewById(R.id.githubLink)

        // Load GitHub Profile Picture using Glide (replace with your GitHub avatar URL)
        Glide.with(this)
            .load("https://avatars.githubusercontent.com/u/118846650?v=4") // Replace with your actual GitHub avatar URL
            .into(githubProfilePicture)

        // Set GitHub link click listener
        githubLink.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/toryang2/lgu_kitaotao_kiosk")) // Replace with your GitHub URL
            startActivity(intent)
        }


        val checkForUpdateButton: Button = findViewById(R.id.buttonCheckUpdate)
        checkForUpdateButton.setOnClickListener {
            // Launching a coroutine to call checkForUpdates
            CoroutineScope(Dispatchers.Main).launch {
                checkForUpdates()
            }
        }

        // Make sure your layout has a main parent with the id "main"
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val versionTextView: TextView = findViewById(R.id.versionTextView)
        versionTextView.text = "v${BuildConfig.VERSION_NAME}"

        val changePasswordButton: Button = findViewById(R.id.buttonChangePassword)
        if (!isTvDevice()) {
            changePasswordButton.visibility = View.INVISIBLE
        } else {
            changePasswordButton.setOnClickListener {
                showPasswordConfirmationDialog()
            }
        }

        // Reset Password Button Logic
        val resetPasswordButton: Button = findViewById(R.id.buttonResetPassword)
        if(!isTvDevice()) {
            resetPasswordButton.visibility = View.INVISIBLE
        } else {
            resetPasswordButton.setOnClickListener {
                resetPassword()
                Toast.makeText(this, "Password reset to default", Toast.LENGTH_SHORT).show()
            }
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

    private fun lockTaskOffWhenUpdate() {
        stopLockTask()
    }

    suspend fun checkForUpdates() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoInternetDialog()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)

        try {
            val release = service.getLatestRelease("toryang2", "lgu_kitaotao_kiosk")
            val latestVersion = release.tag_name.trimStart('v') // Remove 'v' from version
            val currentVersion =
                BuildConfig.VERSION_NAME.split(":")[0] // Only take the part before the colon

            // Compare the versions to check if the GitHub version is newer
            if (isUpdateRequired(latestVersion, currentVersion)) {
                showUpdateDialog(latestVersion, release.assets[0].browser_download_url, release.body)
            } else {
                showNoUpdateDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showNoInternetDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("No Internet Connection")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()

        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK") ("No")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
    }

    fun showNoUpdateDialog() {
        val dialog = AlertDialog.Builder(this)
            .setMessage("You are already on the latest version.")
            .setPositiveButton("OK", null)
            .show()

        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK") ("No")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
    }

    fun isUpdateRequired(latestVersion: String, currentVersion: String): Boolean {
        // Split the version strings into individual components (major, minor, patch)
        val latestVersionParts = latestVersion.split(".").map { it.toInt() }
        val currentVersionParts = currentVersion.split(".").map { it.toInt() }

        // Compare major, minor, and patch versions
        for (i in 0 until 3) {
            if (latestVersionParts[i] > currentVersionParts[i]) {
                return true // GitHub version is higher
            } else if (latestVersionParts[i] < currentVersionParts[i]) {
                return false // Installed version is higher
            }
        }
        return false // Versions are equal
    }

    fun showUpdateDialog(version: String, url: String, description: String) {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Update Available")
            .setMessage("Version $version is available.\n\nDescription: $description\n\nWould you like to download it?")
            .setPositiveButton("Yes") { _, _ ->
                // Show progress dialog
                showProgressDialog()

                // Download the APK
                lockTaskOffWhenUpdate()
                downloadApk(url)
            }
            .setNegativeButton("No", null)
            .show()

        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK") ("No")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(color)
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var progressDialog: AlertDialog
    private lateinit var downloadText: TextView

    // Call this method to show the progress dialog with a progress bar
    private fun showProgressDialog() {
        // Check if the dialog is already visible
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            return // Don't create a new dialog if one is already showing
        }

        val builder = AlertDialog.Builder(this, R.style.SemiTransparentDialog)
        val view = layoutInflater.inflate(R.layout.progress_dialog_layout, null)

        progressBar = view.findViewById(R.id.progressBar)
        percentageText = view.findViewById(R.id.percentageText)
        downloadText = view.findViewById(R.id.dowloading)


        builder.setView(view)
            .setCancelable(false) // Prevent manual dismissal
        progressDialog = builder.create()

        progressDialog.show()
    }

    // Update the progress bar during the download
    private fun downloadApk(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                // Handle error on the UI thread if needed
                runOnUiThread {
                    if (::progressDialog.isInitialized && progressDialog.isShowing) {
                        progressDialog.dismiss()
                    }
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "downloadedfile.apk")

                    val totalSize = response.body?.contentLength() ?: 0
                    var downloadedSize: Long = 0

                    // Show the progress dialog
                    runOnUiThread {
                        showProgressDialog()
                    }

                    response.body?.byteStream()?.use { inputStream ->
                        file.outputStream().use { outputStream ->
                            val buffer = ByteArray(1024)
                            var bytesRead: Int

                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                outputStream.write(buffer, 0, bytesRead)
                                downloadedSize += bytesRead
                                val progress = (downloadedSize * 100 / totalSize).toInt()

                                // Update progress bar and percentage text on the UI thread
                                runOnUiThread {
                                    if (progress <= 100) {
                                        progressBar.progress = progress
                                        percentageText.text = "$progress%"
                                    }
                                }
                            }
                        }
                    }

                    // Once download is complete, dismiss the dialog
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        percentageText.visibility = View.GONE

                        if (::progressDialog.isInitialized && progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }

                        installApk(file) // Proceed with APK installation
                    }
                } else {
                    // Handle unsuccessful response
                    runOnUiThread {
                        if (::progressDialog.isInitialized && progressDialog.isShowing) {
                            progressDialog.dismiss()
                        }
                    }
                }
            }
        })
    }

    private fun installApk(file: File) {
        // Install the APK after download
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = FileProvider.getUriForFile(
                this@about,
                "${packageName}.provider", // Provider authority
                file
            )
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Start the installation intent
        startActivity(intent)
    }
}