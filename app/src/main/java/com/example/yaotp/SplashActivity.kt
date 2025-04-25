package com.example.yaotp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.yaotp.databinding.ActivitySplashBinding
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.view.WindowCompat

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var keepSplashOnScreen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        // Keep the splash screen on-screen while we load
        splashScreen.setKeepOnScreenCondition { keepSplashOnScreen }

        // Make the app full screen
        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Hide the action bar
        supportActionBar?.hide()

        // Fade in animation for the title
        val fadeIn = AlphaAnimation(0f, 1f).apply {
            duration = 1000
            fillAfter = true
        }

        binding.titleText.startAnimation(fadeIn)

        fadeIn.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                // After fade in, wait for a moment and start MainActivity
                Handler(Looper.getMainLooper()).postDelayed({
                    keepSplashOnScreen = false
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }, 1000) // Wait 1 second after fade in
            }
        })
    }
}
