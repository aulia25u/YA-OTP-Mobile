package com.msfauthentication.presentation.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivityDashboardBinding
import com.msfauthentication.presentation.settings.SettingsActivity
import com.msfauthentication.presentation.uploadimage.UploadImageActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {

    private val binding by lazy { ActivityDashboardBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<DashboardViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.apply {
            cvUploadImage.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, UploadImageActivity::class.java))
            }
            cvSettings.setOnClickListener {
                startActivity(Intent(this@DashboardActivity, SettingsActivity::class.java))
            }
        }

        setOnBackPressed()
    }

    private fun setOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        observeFingerprintState()
    }

    private fun observeFingerprintState() {
        viewModel.isFingerprintEnabled().observe(this) { enabled ->
            binding.cvUploadImage.isVisible = enabled
        }
    }
}