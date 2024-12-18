package com.kitaotao.sst

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.airbnb.lottie.LottieAnimationView
import com.kitaotao.sst.SeasonalFallUtility.addSeasonalFall
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import playWithLoopFrom
import showClickPopAnimation
import java.util.Calendar

class postScreen : AppCompatActivity() {

    // Define the ActivityResultLauncher for the device admin activation result
    private lateinit var deviceAdminResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install splash screen with a coroutine-based delay
        installSplashScreen().setKeepOnScreenCondition {
            runBlocking {
                delay(2000L)
                false
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Get the current date
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar.MONTH is 0-indexed
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        if (isTvDevice()) {
            if (currentMonth == 12 && currentDay in 1..31) {  //Christmas

                setContentView(R.layout.activity_post_screen_christmas)

                // Create a root layout for the Activity
                val rootLayout = findViewById<ConstraintLayout>(R.id.main)

                // Add snowfall effect using the utility function
                rootLayout.addSeasonalFall(this)

                // Find your LottieAnimationView from the layout
                val lottieAnimationView = findViewById<LottieAnimationView>(R.id.imageButton)

                // Set the animation resource
                lottieAnimationView.setAnimation(R.raw.lgu_kitaotao_christmas_logo)

                // Play the animation and start looping from a specific point (e.g., 2000 ms)
                val loopStartTimeMs: Long = 4100 // Set the loop start time in milliseconds
                lottieAnimationView.playWithLoopFrom(loopStartTimeMs)
            } else if (currentMonth == 2 && currentDay in 1..14) { //Valentines
                setContentView(R.layout.activity_post_screen)

                // Create a root layout for the Activity
                val rootLayout = findViewById<ConstraintLayout>(R.id.main)

                // Add snowfall effect using the utility function
                rootLayout.addSeasonalFall(this)
            } else if (currentMonth == 11 && currentDay in 1..31) { //Halloween
                setContentView(R.layout.activity_post_screen)

                // Create a root layout for the Activity
                val rootLayout = findViewById<ConstraintLayout>(R.id.main)

                // Add snowfall effect using the utility function
                rootLayout.addSeasonalFall(this)
            } else {
                setContentView(R.layout.activity_post_screen)
            }
        } else {
            setContentView(R.layout.activity_post_screen)
        }

        // Show app version
        val versionTextView: TextView = findViewById(R.id.versionTextView)
        versionTextView.text = "v${BuildConfig.VERSION_NAME}"

        // Initialize the ActivityResultLauncher for handling device admin activation result
        deviceAdminResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->
                if (result.resultCode == RESULT_OK) {
                    if (isDeviceAdminActive()) {
                        Toast.makeText(this, "Device admin activated!", Toast.LENGTH_SHORT).show()
                        configureLockTaskMode() // Configure Lock Task Mode after admin activation
                    } else {
                        Toast.makeText(this, "Device admin activation failed!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        )

        // Prompt to activate device admin if not already enabled
        if (!isDeviceAdminActive()) {
            activateDeviceAdmin()
        } else {
            configureLockTaskMode() // Configure Lock Task Mode only if admin is active
        }

        // Handle back press to prevent user from exiting
        handleBackPress()

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Add click listener for navigation
        setClickListener(R.id.imageButton2, MainActivity::class.java)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (isTvDevice()) {
            showClickPopAnimation(event) // Call the function defined in clickPop.kt
        }
        return super.dispatchTouchEvent(event)
    }

    /**
     * Checks if the app is currently a device admin.
     */
    private fun isDeviceAdminActive(): Boolean {
        if (!isTvDevice()) return false
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
        return devicePolicyManager.isAdminActive(componentName)
    }

    /**
     * Launches the device admin activation screen.
     */
    private fun activateDeviceAdmin() {
        if (!isTvDevice()) return
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)
        val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
            putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
            putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "Please enable device admin to lock the app.")
        }
        // Use the ActivityResultLauncher to start the activity
        deviceAdminResultLauncher.launch(intent)
    }

    /**
     * Configures Lock Task Mode if the app is a device owner.
     */
    private fun configureLockTaskMode() {
        if (!isTvDevice()) return
        val devicePolicyManager = getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val componentName = ComponentName(this, MyDeviceAdminReceiver::class.java)

        // Check if the app is the device owner
        if (devicePolicyManager.isDeviceOwnerApp(packageName)) {
            // Whitelist the app for Lock Task Mode
            devicePolicyManager.setLockTaskPackages(componentName, arrayOf(packageName))
            startLockTask() // Enter Lock Task Mode
        } else {
            Toast.makeText(this, "App is not the device owner!", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Prevents the back button from functioning.
     */
    private fun handleBackPress() {
        if (!isTvDevice()) return
        onBackPressedDispatcher.addCallback(this) {
            Toast.makeText(this@postScreen, "Action not allowed!", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Sets a click listener for the specified button to navigate to another activity.
     */
    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<ImageButton>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
