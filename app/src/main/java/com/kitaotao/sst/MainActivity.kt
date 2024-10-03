package com.kitaotao.sst

import android.content.Intent
import android.content.LocusId
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.kitaotao.sst.office.*
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
        setClickListener(R.id.macco, MunicipalAccountingOffice::class.java)
        setClickListener(R.id.administrator, MunicipalAdministratorOffice::class.java)
        setClickListener(R.id.agriculture, MunicipalAgricultureOffice::class.java)
    }
    private fun setClickListener(viewId: Int, activityClass: Class<*>){
        findViewById<TextView>(viewId).setOnClickListener{
            startActivity(Intent(this, activityClass))
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