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
import com.example.yaotp.databinding.FragmentHomeBinding
import com.example.yaotp.utils.BiometricHelper
import com.example.yaotp.utils.DeviceUtils
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeViewModel
    private lateinit var biometricHelper: BiometricHelper

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getDeviceIMEI()
        } else {
            Snackbar.make(
                binding.root,
                "Phone state permission is required for IMEI",
                Snackbar.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBiometric()
        setupClickListeners()
        observeViewModel()
        checkPermissions()
    }

    private fun setupBiometric() {
        biometricHelper = BiometricHelper(this)
        if (!biometricHelper.isBiometricAvailable(requireContext())) {
            binding.fingerprintButton.visibility = View.GONE
        }
        
        biometricHelper.setupBiometric(
            onSuccess = {
                // On successful fingerprint auth, navigate to upload screen
                navigateToUpload()
            },
            onError = { error ->
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            }
        )
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val username = binding.usernameEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Snackbar.make(
                    binding.root,
                    "Please enter both username and password",
                    Snackbar.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            viewModel.login(username, password)
        }

        binding.fingerprintButton.setOnClickListener {
            biometricHelper.authenticate()
        }
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    // On successful login, navigate to upload screen
                    navigateToUpload()
                },
                onFailure = { exception ->
                    Snackbar.make(
                        binding.root,
                        exception.message ?: "Login failed",
                        Snackbar.LENGTH_LONG
                    ).show()
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
        val imei = DeviceUtils.getDeviceIMEI(requireContext())
        if (imei != null) {
            println("Device IMEI: $imei")
        }
    }

    private fun navigateToUpload() {
        findNavController().navigate(R.id.navigation_upload)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
