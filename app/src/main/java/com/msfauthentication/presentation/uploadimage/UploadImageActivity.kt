package com.msfauthentication.presentation.uploadimage

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.msfauthentication.R
import com.msfauthentication.databinding.ActivityUploadImageBinding
import com.msfauthentication.domain.repository.Result
import com.msfauthentication.presentation.uploadimageresult.UploadImageResultActivity
import com.msfauthentication.presentation.dialog.AlertDialogDefaults
import com.msfauthentication.presentation.dialog.showAlertDialog
import com.msfauthentication.presentation.dialog.showLoadingDialog
import com.msfauthentication.util.toFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadImageActivity : AppCompatActivity() {

    private val binding by lazy { ActivityUploadImageBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<UploadImageViewModel>()
    private var imageUri: Uri? = null

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

        binding.apply {
            btnNext.setOnClickListener { uploadImage(imageUri ?: return@setOnClickListener) }
            cvUploadImage.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    type = "image/*"
                }
                launcherGallery.launch(intent)
            }
        }

        observeUploadImageState()
    }

    private fun observeUploadImageState() {
        viewModel.uploadImageState.observe(this) { result ->
            when (result) {
                is Result.Error -> {
                    showLoadingDialog(false)
                    showAlertDialog(
                        message = result.message,
                        properties = AlertDialogDefaults.alertDialogAutoDismiss(
                            isCancelable = true,
                            isAutoDismiss = false
                        )
                    )
                }
                is Result.Loading -> {
                    showLoadingDialog(true)
                }
                is Result.Success -> {
                    showLoadingDialog(false)
                    navigateToImageResult(imageUri, result.data.hashFragment)
                }
            }
        }
    }

    private fun navigateToImageResult(
        imageUri: Uri?,
        hashFragment: String?
    ) {
        val intent = Intent(this, UploadImageResultActivity::class.java).apply {
            putExtra(UploadImageResultActivity.IMAGE_URI, imageUri)
            putExtra(UploadImageResultActivity.HASH_FRAGMENT, hashFragment)
        }
        startActivity(intent)
    }

    private val launcherGallery = registerForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        if (uri != null) {
            val contentResolver = applicationContext.contentResolver
            val takeFlags =
                Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            contentResolver.takePersistableUriPermission(uri, takeFlags)
            imageUri = uri
            binding.apply {
                ivPhoto.setImageURI(uri)
                tvSelect.isVisible = false
                btnNext.isEnabled = true
            }
        }
    }

    private fun setUpToolbar() {
        binding.apply {
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val imageFile = imageUri.toFile(this)
        viewModel.uploadImage(imageFile)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}