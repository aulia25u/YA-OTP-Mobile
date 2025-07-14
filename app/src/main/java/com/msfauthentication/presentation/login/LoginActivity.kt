package com.msfauthentication.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivityLoginBinding
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.presentation.dashboard.DashboardActivity
import com.msfauthentication.presentation.otp.OTPActivity
import com.msfauthentication.presentation.dialog.AlertDialogDefaults
import com.msfauthentication.util.getCurrentDeviceId
import com.msfauthentication.util.getPhoneModel
import com.msfauthentication.presentation.dialog.showAlertDialog
import com.msfauthentication.presentation.dialog.showLoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<LoginViewModel>()
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initBiometric()

        binding.apply {
            btnLogin.setOnClickListener {
                val deviceId = getCurrentDeviceId()
                val phoneModel = getPhoneModel()
                val username = tietUsername.text.toString().trim()
                val password = tietPassword.text.toString().trim()
                viewModel.login(username, password, deviceId, phoneModel)
            }
        }

        setOnBackPressed()
        observeLoginState()
        observeFingerprintState()
    }

    private fun observeFingerprintState() {
        viewModel.shouldShowFingerprintPrompt().observe(this) { result ->
            when (result) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    if (result.data) {
                        showBiometricPrompt()
                    }
                }
            }
        }
    }

    private fun showBiometricPrompt() {
        if (::biometricPrompt.isInitialized && ::promptInfo.isInitialized) {
            biometricPrompt.authenticate(promptInfo)
        }
    }

    private fun initBiometric() {
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(
            this,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence,
                ) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    navigateToDashboard()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showAlertDialog(
                        message = "Authentication failed. Please try again.",
                        properties = AlertDialogDefaults.alertDialogAutoDismiss(
                            isCancelable = true,
                            isAutoDismiss = false,
                        )
                    )
                }
            }
        )
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Fingerprint login for MSF Authentication")
            .setSubtitle("Log in using your fingerprint")
            .setNegativeButtonText("Use username password")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG
            )
            .build()
    }

    private fun observeLoginState() {
        viewModel.loginState.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoadingDialog(false)
                    showAlertDialog(
                        result.message,
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
                        navigateToOTP()
                    }
                }
            }
        }
    }

    private fun setOnBackPressed() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }

    private fun navigateToOTP() {
        val intent = Intent(this, OTPActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}