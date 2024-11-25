package com.kitaotao.sst

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.office.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install the splash screen
        //val splashScreen = installSplashScreen()

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

        val aboutButton: Button = findViewById(R.id.buttonAbout)
        aboutButton.setOnClickListener {
            val intent = Intent(this, about::class.java)
            startActivity(intent)
        }

        // Hide back and home buttons initially
        listOf(R.id.buttonBack, R.id.buttonHome).forEach {
            findViewById<Button>(it).visibility = View.GONE
        }

        // Set click listeners for different offices
        setClickListener(R.id.macco, MunicipalAccountingOffice::class.java)
        setClickListener(R.id.administrator, MunicipalAdministratorOffice::class.java)
        setClickListener(R.id.agriculture, MunicipalAgricultureOffice::class.java)
        setClickListener(R.id.assessor, MunicipalAssessorsOffice::class.java)
        setClickListener(R.id.munBudget, MunicipalBudgetOffice::class.java)
        setClickListener(R.id.bplo, MunicipalBusinessProcessingAndLicensingOffice::class.java)
        setClickListener(R.id.civilRegistry, MunicipalCivilRegistryOffice::class.java)
        setClickListener(R.id.mdrrmo, MDRRMO::class.java)
        setClickListener(R.id.engineering, MunicipalEngineeringOffice::class.java)
        setClickListener(R.id.menro, MENRO::class.java)
        setClickListener(R.id.gso, MunicipalGeneralServiceOffice::class.java)
        setClickListener(R.id.mho, MunicipalHealthOffice::class.java)
        setClickListener(R.id.hrmo, HumanResourceManagementOffice::class.java)
        setClickListener(R.id.mmo, MunicipalMayorOffice::class.java)
        setClickListener(R.id.bac, BidsAndAwardsCommittee::class.java)
        setClickListener(R.id.ipmr, IPMR::class.java)
        setClickListener(R.id.waterSystem, KitaotaoWaterSystem::class.java)
        setClickListener(R.id.liga, LIGA::class.java)
        setClickListener(R.id.ledipo, LEDIPO::class.java)
        setClickListener(R.id.lydo, LYDO::class.java)
        setClickListener(R.id.tourism, TOURISM::class.java)
        setClickListener(R.id.pwd, PWD::class.java)
        setClickListener(R.id.peso, PESO::class.java)
        setClickListener(R.id.senior, SENIOR::class.java)
        setClickListener(R.id.mpdo, MPDO::class.java)
        setClickListener(R.id.popdev, POPDEV::class.java)
        setClickListener(R.id.sbo, SBO::class.java)
        setClickListener(R.id.mswdo, MSWDO::class.java)
        setClickListener(R.id.treasurer, TREASURER::class.java)


        // Use coroutine to handle splash screen delay
        //CoroutineScope(Dispatchers.Main).launch {
        //    delay(4000L)  // Delay for 4 seconds

            // After the delay, dismiss the splash screen
        // splashScreen.setKeepOnScreenCondition { false }
        //}
    }

    private fun setClickListener(viewId: Int, activityClass: Class<*>) {
        findViewById<TextView>(viewId).setOnClickListener {
            startActivity(Intent(this, activityClass))
        }
    }
}
