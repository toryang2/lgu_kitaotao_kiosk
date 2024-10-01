package com.kitaotao.sst

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.office.MunicipalAccountingOffice
import com.kitaotao.sst.office.MunicipalAdministratorOffice
import com.kitaotao.sst.office.MunicipalAgricultureOffice
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().setKeepOnScreenCondition{
            runBlocking {
                delay(2000L)
                false
            }
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        val maccoServices  = findViewById<TextView>(R.id.macco)
        maccoServices.setOnClickListener {
            val intent = Intent(this, MunicipalAccountingOffice::class.java)
            startActivity(intent)
        }

        val adminstratorService = findViewById<TextView>(R.id.administrator)
        adminstratorService.setOnClickListener {
            val intent = Intent(this, MunicipalAdministratorOffice::class.java)
            startActivity(intent)
        }

        val agricultureService = findViewById<TextView>(R.id.agriculture)
        agricultureService.setOnClickListener {
            val intent = Intent(this, MunicipalAgricultureOffice::class.java)
            startActivity(intent)
        }
    }
}

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startActivity(Intent(this@SplashScreenActivity, MainActivity::class.java))
        finish()
    }

}