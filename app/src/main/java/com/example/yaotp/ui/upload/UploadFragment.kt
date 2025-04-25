package com.example.yaotp.ui.upload

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yaotp.databinding.FragmentUploadBinding
import com.google.android.material.snackbar.Snackbar

class UploadFragment : Fragment() {

    private var _binding: FragmentUploadBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UploadViewModel

    private val pickImage = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                binding.imagePreview.setImageURI(uri)
                binding.uploadButton.isEnabled = true
                viewModel.setSelectedImage(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[UploadViewModel::class.java]
        _binding = FragmentUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        observeViewModel()
    }

    private fun setupClickListeners() {
        binding.pickImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImage.launch(intent)
        }

        binding.uploadButton.setOnClickListener {
            viewModel.uploadImage()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.loadingProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.uploadButton.isEnabled = !isLoading
            binding.pickImageButton.isEnabled = !isLoading
        }

        viewModel.uploadResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = { message ->
                    Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
                    // Reset UI after successful upload
                    binding.imagePreview.setImageResource(0)
                    binding.uploadButton.isEnabled = false
                    viewModel.setSelectedImage(null)
                },
                onFailure = { exception ->
                    Snackbar.make(
                        binding.root,
                        exception.message ?: "Upload failed",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
