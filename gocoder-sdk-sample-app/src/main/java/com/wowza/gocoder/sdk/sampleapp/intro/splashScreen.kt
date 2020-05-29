package com.wowza.gocoder.sdk.sampleapp.intro

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.wowza.gocoder.sdk.sampleapp.R

class splashScreen : AppCompatActivity() {
    public val SPLASH_TIME_OUT: Long = 2000 // 2 sec

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window
            w.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        //Handler for splash
        Handler().postDelayed({
            startActivity(Intent(this, checkActivity::class.java))
            // close this activity
            finish()
        }, SPLASH_TIME_OUT)
    }
}
