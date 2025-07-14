package com.msfauthentication.presentation.otp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivityOtpBinding
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.presentation.dashboard.DashboardActivity
import com.msfauthentication.presentation.dialog.AlertDialogDefaults
import com.msfauthentication.presentation.dialog.showAlertDialog
import com.msfauthentication.presentation.dialog.showLoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OTPActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOtpBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<OTPViewModel>()

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
            btnSendOtp.setOnClickListener {
                val otp = etOtp.text.toString().trim()
                viewModel.verifyLogin(otp)
            }
            etOtp.setOtpCompletionListener {
                val otp = it.toString().trim()
                viewModel.verifyLogin(otp)
            }
        }

        setUpToolbar()
        observeOtpState()
    }

    private fun setUpToolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun observeOtpState() {
        viewModel.otpState.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoadingDialog(false)
                    showAlertDialog(
                        message = result.message,
                        properties = AlertDialogDefaults.alertDialogAutoDismiss(
                            isCancelable = true,
                            isAutoDismiss = false,
                        )
                    )
                }
                is Result.Loading -> {
                    showLoadingDialog(true)
                }
                is Result.Success -> {
                    showLoadingDialog(false)
                    showAlertDialog(result.data.message) {
                        navigateToDashboard()
                    }
                }
            }
        }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}