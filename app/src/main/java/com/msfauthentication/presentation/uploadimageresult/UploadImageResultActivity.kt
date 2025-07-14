package com.msfauthentication.presentation.uploadimageresult

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivityUploadImageResultBinding
import com.msfauthentication.presentation.dashboard.DashboardActivity
import com.msfauthentication.util.toFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadImageResultActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUploadImageResultBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setUpToolbar()

        val imageUri = IntentCompat.getParcelableExtra(intent, IMAGE_URI, Uri::class.java)
        val hashFragment = intent.getStringExtra(HASH_FRAGMENT)

        binding.apply {
            ivPhoto.setImageURI(imageUri)
            tvSecretCode.text = hashFragment

            btnNext.setOnClickListener { navigateToDashboard() }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun setUpToolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val IMAGE_URI = "imageUri"
        const val HASH_FRAGMENT = "hashFragment"
    }
}