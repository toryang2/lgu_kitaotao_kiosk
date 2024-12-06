package com.kitaotao.sst

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kitaotao.sst.office.*
import com.kitaotao.sst.model.Release
import com.kitaotao.sst.network.GitHubService
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
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for updates asynchronously when the app starts
        CoroutineScope(Dispatchers.Main).launch {
            checkForUpdates()
        }

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
            GridItem("Municipal Civil Registry Office", getDrawable(R.drawable.mcro256)!!, MunicipalCivilRegistryOffice::class.java),
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

        val layoutManager = GridLayoutManager(this, 10)
        recyclerView.layoutManager = layoutManager

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
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Toast.makeText(this@MainActivity, "Action not allowed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_HOME || keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
            showAdminPasswordDialog()
            return true
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

    private fun readPassword(): String {
        return try {
            openFileInput("admin_password.txt").bufferedReader().use {
                it.readText()
            }
        } catch (e: FileNotFoundException) {
            "adminkitaotao"  // Return default password if not set
        }
    }

    private fun exitKioskMode() {
        stopLockTask()

        Toast.makeText(this, "Exited Kiosk Mode", Toast.LENGTH_SHORT).show()

        finishAffinity()
    }

    private fun lockTaskOffWhenUpdate() {
        stopLockTask()
    }

    suspend fun checkForUpdates() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GitHubService::class.java)

        try {
            val release = service.getLatestRelease("toryang2", "lgu_kitaotao_kiosk")
            val latestVersion = release.tag_name.trimStart('v') // Remove 'v' from version
            val currentVersion = BuildConfig.VERSION_NAME.split(":")[0] // Only take the part before the colon

            // Compare the versions to check if the GitHub version is newer
            if (isUpdateRequired(latestVersion, currentVersion)) {
                showUpdateDialog(latestVersion, release.assets[0].browser_download_url)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

    fun showUpdateDialog(version: String, url: String) {
        AlertDialog.Builder(this)
            .setTitle("Update Available")
            .setMessage("Version $version is available. Would you like to download it?")
            .setPositiveButton("Yes") { _, _ ->
                // Show progress dialog
                showProgressDialog()

                // Download the APK
                lockTaskOffWhenUpdate()
                downloadApk(url)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private lateinit var progressBar: ProgressBar
    private lateinit var percentageText: TextView
    private lateinit var progressDialog: AlertDialog

    // Call this method to show the progress dialog with a progress bar
    private fun showProgressDialog() {
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.progress_dialog_layout, null)

        progressBar = view.findViewById(R.id.progressBar)
        percentageText = view.findViewById(R.id.percentageText)

        builder.setView(view)
            .setCancelable(false) // To make sure the dialog can't be dismissed manually
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

                    // Copy inputStream to outputStream and update progress
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
                                    progressBar.progress = progress
                                    percentageText.text = "$progress%"

                                    // If progress is 100%, dismiss the dialog
                                    if (progress == 100) {
                                        progressDialog.dismiss()  // Dismiss the dialog
                                        installApk(file)  // Proceed with the APK installation
                                    }
                                }
                            }
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
                this@MainActivity,
                "${packageName}.provider", // Provider authority
                file
            )
            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Start the installation intent
        startActivity(intent)
    }
}
