package com.example.yaotp.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.yaotp.R
import com.example.yaotp.databinding.FragmentHomeNewBinding
import com.example.yaotp.utils.BiometricHelper
import com.example.yaotp.utils.DeviceUtils
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeNewBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private var biometricHelper: BiometricHelper? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getDeviceIMEI()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeNewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        try {
            // Initialize ViewModel
            viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
            
            // Setup UI
            setupUI()
            
            // Request permissions after a delay to ensure UI is ready
            view.post {
                checkPermissions()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Failed to initialize: ${e.message}")
        }
    }

    private fun setupUI() {
        setupBiometric()
        setupClickListeners()
        observeViewModel()
    }

    private fun setupBiometric() {
        try {
            context?.let { ctx ->
                // Initialize biometric helper
                biometricHelper = BiometricHelper(this)
                
                // Check if biometric is available
                if (biometricHelper?.isBiometricAvailable(ctx) == true) {
                    binding.fingerprintButton.visibility = View.VISIBLE
                    
                    biometricHelper?.setupBiometric(
                        onSuccess = {
                            navigateToUpload()
                        },
                        onError = { error ->
                            showError(error)
                        }
                    )
                } else {
                    binding.fingerprintButton.visibility = View.GONE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            binding.fingerprintButton.visibility = View.GONE
            showError("Biometric not available: ${e.message}")
        }
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                showError("Please enter both username and password")
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }

        binding.fingerprintButton.setOnClickListener {
            try {
                biometricHelper?.authenticate()
            } catch (e: Exception) {
                e.printStackTrace()
                showError("Biometric authentication failed: ${e.message}")
            }
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { message ->
                    showMessage(message)
                    navigateToUpload()
                },
                onFailure = { exception ->
                    showError(exception.message ?: "Login failed")
                }
            )
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginButton.isEnabled = !isLoading
            binding.fingerprintButton.isEnabled = !isLoading
        }
    }

    private fun checkPermissions() {
        if (!DeviceUtils.hasReadPhoneStatePermission(requireContext())) {
            requestPermissionLauncher.launch(Manifest.permission.READ_PHONE_STATE)
        } else {
            getDeviceIMEI()
        }
    }

    private fun getDeviceIMEI() {
        try {
            val deviceId = DeviceUtils.getDeviceIMEI(requireContext())
            if (deviceId != null) {
                println("Device ID: $deviceId")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun navigateToUpload() {
        try {
            findNavController().navigate(R.id.action_home_to_upload)
        } catch (e: Exception) {
            e.printStackTrace()
            showError("Navigation failed: ${e.message}")
        }
    }

    private fun showError(message: String) {
        _binding?.let { binding ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun showMessage(message: String) {
        _binding?.let { binding ->
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        biometricHelper = null
    }
}
