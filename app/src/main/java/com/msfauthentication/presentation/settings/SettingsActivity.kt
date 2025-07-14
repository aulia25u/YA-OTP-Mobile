package com.msfauthentication.presentation.settings

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivitySettingsBinding
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.presentation.dialog.AlertDialogDefaults
import com.msfauthentication.presentation.dialog.showAlertDialog
import com.msfauthentication.presentation.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executor

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private val binding by lazy { ActivitySettingsBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SettingsViewModel>()
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
        observeFingerprintState()

        binding.apply {
            cvLogout.setOnClickListener {
                viewModel.logout()
            }
        }

        observeLogoutState()
    }

    private fun observeFingerprintState() {
        viewModel.isFingerprintEnabled().observe(this) { enabled ->
            binding.switchFingerprint.isChecked = enabled
            binding.switchFingerprint.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val biometricManager = BiometricManager.from(this)
                    when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
                        BiometricManager.BIOMETRIC_SUCCESS -> {}
                        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                            showAlertDialog(
                                message = "No biometric features available on this device.",
                                properties = AlertDialogDefaults.alertDialogAutoDismiss(
                                    isCancelable = true,
                                    isAutoDismiss = false,
                                )
                            )
                        }
                        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                            showAlertDialog(
                                message = "Biometric features are currently unavailable.",
                                properties = AlertDialogDefaults.alertDialogAutoDismiss(
                                    isCancelable = true,
                                    isAutoDismiss = false,
                                )
                            )
                        }
                        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                                    putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                                        BIOMETRIC_STRONG)
                                }
                                launcherEnrollIntent.launch(enrollIntent)
                            }
                        }
                        else -> Unit
                    }
                    showBiometricPrompt()
                } else {
                    viewModel.setFingerprintEnabled(false)
                }
            }
        }
    }

    private val launcherEnrollIntent = registerForActivityResult(
      contract = ActivityResultContracts.StartActivityForResult()
    ) {

    }

    private fun observeLogoutState() {
        viewModel.logoutState.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showAlertDialog(
                        message = result.message,
                        properties = AlertDialogDefaults.alertDialogAutoDismiss(
                            isCancelable = true,
                            isAutoDismiss = false,
                        )
                    )
                }
                is Result.Loading -> {}
                is Result.Success -> {
                    showAlertDialog(
                        message = result.data.message,
                    ) {
                        navigateToLogin()
                    }
                }
            }
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
                    binding.switchFingerprint.isChecked = false
                    showAlertDialog(
                        message = "Authentication error: $errString. Code: $errorCode",
                        properties = AlertDialogDefaults.alertDialogAutoDismiss(
                            isCancelable = true,
                            isAutoDismiss = false,
                        )
                    )
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult,
                ) {
                    super.onAuthenticationSucceeded(result)
                    viewModel.setFingerprintEnabled(true)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    binding.switchFingerprint.isChecked = false
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
            .setTitle("Fingerprint for MSF Authentication")
            .setSubtitle("Enable fingerprint")
            .setAllowedAuthenticators(
                BIOMETRIC_STRONG
            )
            .setNegativeButtonText("Cancel")
            .build()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun showBiometricPrompt() {
        if (::biometricPrompt.isInitialized && ::promptInfo.isInitialized) {
            biometricPrompt.authenticate(promptInfo)
        }
    }
}