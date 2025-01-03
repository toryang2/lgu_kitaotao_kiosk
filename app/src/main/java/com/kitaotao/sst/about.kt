package com.kitaotao.sst

import addSeasonalBackground
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.kitaotao.sst.model.Release
import com.kitaotao.sst.network.GitHubService
import com.kitaotao.sst.utils.NetworkUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import showClickPopAnimation
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class about : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.about)

        addSeasonalBackground()

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

        // Inside your activity's onCreate method or wherever you're setting up the UI
        val changelogButton: Button = findViewById(R.id.buttonChangelog)
        changelogButton.setOnClickListener {
            // Trigger the changelog fetch when the button is clicked
            CoroutineScope(Dispatchers.Main).launch {
                showChangelogFromGitHub()
            }
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

    suspend fun fetchAllReleases(owner: String, repo: String): List<Release> {
        val allReleases = mutableListOf<Release>()
        var currentPage = 1
        var hasMoreReleases = true

        // Initialize Retrofit and create the service instance
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/") // GitHub API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create the GitHubService instance
        val service = retrofit.create(GitHubService::class.java)

        while (hasMoreReleases) {
            try {
                // Fetch releases for the current page with pagination
                val releases = service.getReleases(owner, repo, page = currentPage, perPage = 100)

                // Add the fetched releases to the list
                allReleases.addAll(releases)

                // If the number of releases is less than 100, we are on the last page
                hasMoreReleases = releases.size == 100
                currentPage++
            } catch (e: Exception) {
                e.printStackTrace()
                break
            }
        }

        return allReleases
    }

    // Function to fetch and display the changelog
    suspend fun showChangelogFromGitHub() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            showNoInternetDialog()
            return
        }

        try {
            // Fetch all releases from GitHub
            val releases = fetchAllReleases("toryang2", "lgu_kitaotao_kiosk")

            // Use the version from BuildConfig and split to get the part before the colon
            val currentVersion = BuildConfig.VERSION_NAME.split(":")[0] // Only take the part before the colon

            // Split versions into parts for comparison (major, minor, patch)
            val currentVersionParts = currentVersion.split(".").map { it.toInt() }

            // Find the release that matches the current version
            val matchingRelease = releases.find { release ->
                val releaseVersionParts = release.tag_name.trimStart('v').split(".").map { it.toInt() }
                releaseVersionParts == currentVersionParts
            }

            if (matchingRelease != null) {
                val version = matchingRelease.tag_name.trimStart('v')
                val changelogDescription = matchingRelease.body

                // Show the changelog dialog with this specific version's changelog
                showChangelogDialog(version, changelogDescription)
            } else {
                // No matching version found, show an alternative dialog
                showNoMatchingVersionDialog()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Optionally handle errors, such as network failures
        }
    }

    // Function to show changelog dialog with the specific version's changelog
    fun showChangelogDialog(version: String, description: String) {
        val changelogMessage = """
                    
        $description
        
    """

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Whats new of v$version?")
            .setMessage(changelogMessage)
            .setPositiveButton("Got it") { dialog, _ ->
                dialog.dismiss()
            }

        val alertDialog = builder.create()
        alertDialog.show()

        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK") ("No")
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
    }

    fun showNoMatchingVersionDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("No Matching Version")
            .setMessage("No matching version found on GitHub for the current installed version.")
            .setPositiveButton("OK", null)
            .show()

        val color = Color.parseColor("#49454F") // Your desired color for the button text

        // Change the text color of the positive button ("OK") ("No")
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(color)
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

    private var downloadCall: Call? = null

    private lateinit var progressBar: ProgressBar
    private lateinit var progressDialog: AlertDialog
    private lateinit var downloadText: TextView

    // Call this method to show the progress dialog with a progress bar
    private fun showProgressDialog() {
        // Check if the dialog is already visible
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            return
        }

        val builder = AlertDialog.Builder(this, R.style.SemiTransparentDialog)
        val view = layoutInflater.inflate(R.layout.download_bar_new, null)

        progressBar = view.findViewById(R.id.progressBar)
        downloadText = view.findViewById(R.id.dowloading)

        // Customization for APK installer style
        downloadText.text = "Preparing download..." // Initial text
        progressBar.max = 100 // Ensure the progress bar has a max value of 100

        builder.setView(view)
            .setCancelable(true) // Prevent manual dismissal
            .setOnDismissListener {
                downloadCall?.cancel()
            }
        progressDialog = builder.create()

        progressDialog.show()

        val window = progressDialog.window
        window?.setLayout(800, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    // Update progress dynamically
    private fun updateProgress(progress: Int) {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressBar.progress = progress
            downloadText.text = if (progress < 100) "Downloading..." else "Download complete!"
        }
    }

    // Call this method to dismiss the progress dialog
    private fun dismissProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
        downloadCall?.cancel()
    }

    private fun downloadApk(url: String) {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        // Store the download call reference
        downloadCall = client.newCall(request)

        downloadCall?.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    dismissProgressDialog()
                    Toast.makeText(this@about, "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "downloadedfile.apk")
                    val totalSize = response.body?.contentLength() ?: 0

                    if (totalSize <= 0) {
                        runOnUiThread {
                            dismissProgressDialog()
                            Toast.makeText(this@about, "Invalid file size", Toast.LENGTH_SHORT).show()
                        }
                        return
                    }

                    var downloadedSize: Long = 0
                    runOnUiThread { showProgressDialog() }

                    try {
                        response.body?.byteStream()?.use { inputStream ->
                            file.outputStream().use { outputStream ->
                                val buffer = ByteArray(1024)
                                var bytesRead: Int

                                while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                                    outputStream.write(buffer, 0, bytesRead)
                                    downloadedSize += bytesRead
                                    val progress = (downloadedSize * 100 / totalSize).toInt()

                                    runOnUiThread { updateProgress(progress) }
                                }
                            }
                        }

                        runOnUiThread {
                            dismissProgressDialog()
                            Toast.makeText(this@about, "Download complete!", Toast.LENGTH_SHORT).show()
                            installApk(file)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        runOnUiThread {
                            dismissProgressDialog()
                            Toast.makeText(this@about, "Error during download: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        dismissProgressDialog()
                        Toast.makeText(this@about, "Server error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

//    // Update the progress bar during the download
//    private fun downloadApk(url: String) {
//        val client = OkHttpClient()
//        val request = Request.Builder().url(url).build()
//
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                e.printStackTrace()
//                // Handle error on the UI thread if needed
//                runOnUiThread {
//                    if (::progressDialog.isInitialized && progressDialog.isShowing) {
//                        progressDialog.dismiss()
//                    }
//                }
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                if (response.isSuccessful) {
//                    val file = File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "downloadedfile.apk")
//
//                    val totalSize = response.body?.contentLength() ?: 0
//                    var downloadedSize: Long = 0
//
//                    // Show the progress dialog
//                    runOnUiThread {
//                        showProgressDialog()
//                    }
//
//                    response.body?.byteStream()?.use { inputStream ->
//                        file.outputStream().use { outputStream ->
//                            val buffer = ByteArray(1024)
//                            var bytesRead: Int
//
//                            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
//                                outputStream.write(buffer, 0, bytesRead)
//                                downloadedSize += bytesRead
//                                val progress = (downloadedSize * 100 / totalSize).toInt()
//
//                                // Update progress bar and percentage text on the UI thread
//                                runOnUiThread {
//                                    if (progress <= 100) {
//                                        progressBar.progress = progress
//                                    }
//                                }
//                            }
//                        }
//                    }
//
//                    // Once download is complete, dismiss the dialog
//                    runOnUiThread {
//                        progressBar.visibility = View.GONE
//
//                        if (::progressDialog.isInitialized && progressDialog.isShowing) {
//                            progressDialog.dismiss()
//                        }
//
//                        installApk(file) // Proceed with APK installation
//                    }
//                } else {
//                    // Handle unsuccessful response
//                    runOnUiThread {
//                        if (::progressDialog.isInitialized && progressDialog.isShowing) {
//                            progressDialog.dismiss()
//                        }
//                    }
//                }
//            }
//        })
//    }

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

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isTvDevice()) {
            showClickPopAnimation(event) // Call the function defined in clickPop.kt
        }
        return super.dispatchTouchEvent(event)
    }
}