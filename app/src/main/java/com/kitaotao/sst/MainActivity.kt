package com.kitaotao.sst

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.office.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen
        val splashScreen = installSplashScreen()

        // Call super before using any view in your activity
        super.onCreate(savedInstanceState)

        // Set up edge-to-edge display
        enableEdgeToEdge()
        setContentView(R.layout.activity_main) // Set your main layout here

        // Set padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Hide back and home buttons initially
        listOf(R.id.buttonBack, R.id.buttonHome).forEach {
            findViewById<Button>(it).visibility = View.GONE
        }

        // Set click listeners for different offices
        setClickListener(R.id.macco, MunicipalAccountingOffice::class.java)
        setClickListener(R.id.administrator, MunicipalAdministratorOffice::class.java)
        setClickListener(R.id.agriculture, MunicipalAgricultureOffice::class.java)

        // Use coroutine to handle splash screen delay
        CoroutineScope(Dispatchers.Main).launch {
            delay(5000L)  // Delay for 5 seconds

            // After the delay, dismiss the splash screen
            splashScreen.setKeepOnScreenCondition { false }
        }
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
